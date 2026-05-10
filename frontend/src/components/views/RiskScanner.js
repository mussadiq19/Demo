import React, { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { riskService } from '../../services/riskService';

const severityClass = (severity) => {
  const normalized = String(severity || '').toUpperCase();
  if (normalized === 'CRITICAL') return 'crit';
  if (normalized === 'HIGH') return 'high';
  if (normalized === 'MEDIUM') return 'med';
  return 'low';
};

const formatDetected = (value) => {
  if (!value) return 'unknown';
  return new Date(value).toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
};

const RiskScanner = () => {
  const [activeTab, setActiveTab] = useState('All Risks');
  const [selectedRisk, setSelectedRisk] = useState(null);
  const { user } = useAuth();
  const queryClient = useQueryClient();

  const tabs = [];

  const { data: risksPage, isLoading } = useQuery({
    queryKey: ['risks-page', user?.companyId],
    queryFn: () => riskService.getRisks({
      companyId: user.companyId,
      status: 'OPEN',
      page: 0,
      size: 20,
    }),
    enabled: !!user?.companyId,
  });

  const { data: stats } = useQuery({
    queryKey: ['risk-stats', user?.companyId],
    queryFn: () => riskService.getStats(user.companyId),
    enabled: !!user?.companyId,
  });

  const risks = risksPage?.content || [];

  const scanMutation = useMutation({
    mutationFn: () => riskService.triggerScan(user.companyId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['risks-page'] });
      queryClient.invalidateQueries({ queryKey: ['risks'] });
      queryClient.invalidateQueries({ queryKey: ['risk-stats'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const acknowledgeMutation = useMutation({
    mutationFn: riskService.acknowledge,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['risks-page'] });
      queryClient.invalidateQueries({ queryKey: ['risks'] });
      queryClient.invalidateQueries({ queryKey: ['risk-stats'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const resolveMutation = useMutation({
    mutationFn: riskService.resolve,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['risks-page'] });
      queryClient.invalidateQueries({ queryKey: ['risks'] });
      queryClient.invalidateQueries({ queryKey: ['risk-stats'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  return (
    <div id="view-risks">
      <div className="tabs" style={{ marginBottom: '4px' }}>
        {tabs.map((tab) => (
          <div
            key={tab}
            className={`tab ${activeTab === tab ? 'active' : ''}`}
            onClick={() => setActiveTab(tab)}
          >
            {tab}
          </div>
        ))}
      </div>
      
      <div className="full-card">
        <div className="card-head">
          <div>
            <div className="card-title">Active Risk Signals</div>
            <div className="card-sub">{stats?.open ?? risksPage?.totalElements ?? risks.length} open · {stats?.critical ?? 0} critical · {stats?.high ?? 0} high</div>
          </div>
            <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <div className="sovai-badge">⚡ Foresight AI</div>
            <button
              className="btn btn-scan" 
              style={{ fontSize: '11px', padding: '6px 12px' }}
              onClick={() => scanMutation.mutate()}
              disabled={!user?.companyId || scanMutation.isPending}
            >
              <span style={{ fontSize: '13px' }}>📡</span>
              {scanMutation.isPending ? 'Scanning...' : 'Run AI Scan'}
            </button>
          </div>
        </div>
        
        {isLoading ? (
          <div style={{ padding: '10px 0', fontSize: '12px', color: 'var(--text3)' }}>Loading risks...</div>
        ) : risks.map((risk, index) => (
          <div 
            key={risk.id} 
            className="risk-row" 
            style={{ 
              padding: '10px 0',
              borderBottom: index === risks.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <span className={`severity-pill ${severityClass(risk.severity)}`}>
              {risk.severity}
            </span>
            <div style={{ flex: 1, padding: '0 10px' }}>
              <div className="risk-title" style={{ fontSize: '13px', marginBottom: '2px' }}>
                {risk.title}
              </div>
              <div style={{ fontSize: '11px', color: 'var(--text3)' }}>
                Source: {risk.source || 'AI scan'} · {risk.categoryId ? `Category ${risk.categoryId}` : 'Risk'} · Detected {formatDetected(risk.detectedAt)}
              </div>
              {risk.description && (
                <div className="ai-insight" style={{ marginTop: '6px', padding: '8px 10px' }}>
                  <div className="ai-insight-text">{risk.description}</div>
                </div>
              )}
            </div>
            <div style={{ 
              display: 'flex', 
              flexDirection: 'column', 
              gap: '5px', 
              alignItems: 'flex-end', 
              flexShrink: 0 
            }}>
              {risk.status === 'ACKNOWLEDGED' ? (
                <span style={{ fontSize: '11px', color: '#6366f1', fontWeight: '500', padding: '4px 0' }}>
                  ✓ Acknowledged
                </span>
              ) : (
                <>
                  <button 
                    className="btn btn-ghost" 
                    style={{ fontSize: '11px', padding: '4px 10px' }}
                    onClick={() => acknowledgeMutation.mutate(risk.id)}
                    disabled={acknowledgeMutation.isPending}
                  >
                    Acknowledge
                  </button>
                  <button 
                    className="btn btn-primary" 
                    style={{ fontSize: '11px', padding: '4px 10px' }}
                    onClick={() => setSelectedRisk(risk)}
                  >
                    Action Plan
                  </button>
                  <button 
                    className="btn btn-ghost" 
                    style={{ fontSize: '11px', padding: '4px 10px' }}
                    onClick={() => resolveMutation.mutate(risk.id)}
                    disabled={resolveMutation.isPending}
                  >
                    Resolve
                  </button>
                </>
              )}
            </div>
          </div>
        ))}
      </div>

      {selectedRisk && (
        <div
          className="full-card"
          style={{ marginTop: '8px', padding: '12px' }}
        >
          <div className="card-head">
            <div>
              <div className="card-title">Action Plan</div>
              <div className="card-sub">{selectedRisk.title}</div>
            </div>
            <button className="btn btn-ghost" style={{ fontSize: '11px', padding: '4px 10px' }} onClick={() => setSelectedRisk(null)}>
              Close
            </button>
          </div>
          <div className="ai-insight-text">
            {selectedRisk.mitigation || selectedRisk.description || 'No mitigation has been provided for this risk yet.'}
          </div>
        </div>
      )}
    </div>
  );
};

export default RiskScanner;
