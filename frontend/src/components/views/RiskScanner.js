import React, { useState } from 'react';

const RiskScanner = () => {
  const [activeTab, setActiveTab] = useState('All Risks');
  
  const tabs = [];
  
  const riskSignals = [
    {
      severity: 'crit',
      severityLabel: 'Critical',
      title: 'TSMC supply disruption — Q3 chip shortage',
      source: 'Reuters',
      category: 'Supply Chain',
      detected: 'May 7',
      insight: 'Prepare by pre-ordering 30% additional inventory by June 15 or qualify MediaTek as alternative. Your ops team is well-positioned to manage this.',
      acknowledged: false
    },
    {
      severity: 'high',
      severityLabel: 'High',
      title: 'React 19 legacy API deprecation',
      source: 'GitHub',
      category: 'Technology',
      detected: 'May 6',
      insight: '',
      acknowledged: false
    },
    {
      severity: 'high',
      severityLabel: 'High',
      title: 'DPDP Act compliance gap — data residency',
      source: 'MeitY',
      category: 'Compliance',
      detected: 'May 5',
      insight: '',
      acknowledged: false
    },
    {
      severity: 'med',
      severityLabel: 'Medium',
      title: 'Competitor pricing pressure — SMB segment erosion',
      source: 'G2 Reviews',
      category: 'Market',
      detected: 'May 4',
      insight: '',
      acknowledged: true
    }
  ];

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
            <div className="card-sub">14 open · 3 acknowledged · Last scan 4 min ago</div>
          </div>
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <div className="sovai-badge">⚡ SovAI</div>
            <button 
              className="btn btn-scan" 
              style={{ fontSize: '11px', padding: '6px 12px' }}
            >
              <span style={{ fontSize: '13px' }}>📡</span>
              Run AI Scan
            </button>
          </div>
        </div>
        
        {riskSignals.map((risk, index) => (
          <div 
            key={index} 
            className="risk-row" 
            style={{ 
              padding: '10px 0',
              borderBottom: index === riskSignals.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <span className={`severity-pill ${risk.severity}`}>
              {risk.severityLabel}
            </span>
            <div style={{ flex: 1, padding: '0 10px' }}>
              <div className="risk-title" style={{ fontSize: '13px', marginBottom: '2px' }}>
                {risk.title}
              </div>
              <div style={{ fontSize: '11px', color: 'var(--text3)' }}>
                Source: {risk.source} · {risk.category} · Detected {risk.detected}
              </div>
              {risk.insight && (
                <div className="ai-insight" style={{ marginTop: '6px', padding: '8px 10px' }}>
                  <div className="ai-insight-text">{risk.insight}</div>
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
              {risk.acknowledged ? (
                <span style={{ fontSize: '11px', color: '#6366f1', fontWeight: '500', padding: '4px 0' }}>
                  ✓ Acknowledged
                </span>
              ) : (
                <>
                  <button 
                    className="btn btn-ghost" 
                    style={{ fontSize: '11px', padding: '4px 10px' }}
                  >
                    Acknowledge
                  </button>
                  <button 
                    className="btn btn-primary" 
                    style={{ fontSize: '11px', padding: '4px 10px' }}
                  >
                    Action Plan
                  </button>
                </>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default RiskScanner;
