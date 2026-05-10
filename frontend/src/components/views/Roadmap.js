import React from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { roadmapService } from '../../services/roadmapService';

const Roadmap = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const { data: roadmap, isLoading } = useQuery({
    queryKey: ['roadmap', user?.id],
    queryFn: () => roadmapService.getRoadmap(user.id),
    enabled: !!user?.id,
  });

  const completeMutation = useMutation({
    mutationFn: roadmapService.completeStep,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['roadmap', user?.id] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const roadmapSteps = roadmap?.steps || [];
  const completedSteps = roadmapSteps.filter(step => step.completed).length;
  const totalSteps = roadmapSteps.length;
  const progressPercentage = totalSteps ? Math.round((completedSteps / totalSteps) * 100) : 0;
  const firstIncompleteId = roadmapSteps.find((step) => !step.completed)?.id;

  return (
    <div id="view-roadmap">
      <div 
        className="scan-banner" 
        style={{ 
          background: 'linear-gradient(135deg,#0a1a0f,#0f1117)', 
          marginBottom: '2px' 
        }}
      >
        <div className="scan-icon" style={{ background: 'rgba(34,197,94,0.2)' }}>
          <span style={{ fontSize: '18px', color: '#4ade80' }}>🗺️</span>
        </div>
        <div className="scan-text">
          <div className="t1">Your personalised growth path — built to keep you ahead.</div>
          <div className="t2">⚡ Powered by SovAI · User {user?.id ?? ''} · {roadmap?.title || 'Roadmap'} · Backend synced</div>
        </div>
        <button 
          className="btn" 
          style={{ 
            marginLeft: 'auto', 
            background: '#22c55e', 
            color: '#fff', 
            fontSize: '11px', 
            padding: '6px 12px' 
          }}
        >
          <span style={{ fontSize: '13px' }}>🔄</span>
          Regenerate
        </button>
      </div>
      
      <div className="full-card">
        <div className="card-head">
          <div>
            <div className="card-title">{roadmap?.title || 'AI/ML Upskilling Roadmap'}</div>
            <div className="card-sub">
              {completedSteps} of {totalSteps} steps complete
            </div>
          </div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <div style={{ fontSize: '12px', fontWeight: '600', color: '#22c55e' }}>
              {progressPercentage}%
            </div>
            <div style={{ 
              width: '120px', 
              height: '6px', 
              background: 'var(--surface2)', 
              borderRadius: '3px', 
              overflow: 'hidden' 
            }}>
              <div style={{ 
                width: `${progressPercentage}%`, 
                height: '100%', 
                background: '#22c55e', 
                borderRadius: '3px' 
              }}></div>
            </div>
          </div>
        </div>
        
        {isLoading ? (
          <div style={{ padding: '8px 0', fontSize: '12px', color: 'var(--text3)' }}>Loading roadmap...</div>
        ) : roadmapSteps.map((step, index) => {
          const current = step.id === firstIncompleteId;
          return (
          <div 
            key={step.id}
            className={`roadmap-step ${
              current ? 'current' : ''
            } ${
              !step.completed && !current ? 'upcoming' : ''
            }`}
            style={{
              background: current ? 'rgba(99,102,241,0.03)' : 'transparent',
              borderRadius: current ? '8px' : '0',
              padding: current ? '8px 10px' : '8px 0',
              margin: current ? '0 -10px' : '0',
              opacity: !step.completed && !current ? '0.5' : '1',
              borderBottom: index === roadmapSteps.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <button
              type="button"
              className={`step-check ${step.completed ? 'done' : ''}`}
              style={{ 
                borderColor: current ? '#6366f1' : 'var(--border2)',
                background: step.completed ? '#22c55e' : 'transparent'
              }}
              onClick={() => !step.completed && completeMutation.mutate(step.id)}
              disabled={step.completed || completeMutation.isPending}
            >
              {step.completed && (
                <span style={{ fontSize: '11px', color: '#fff' }}>✓</span>
              )}
              {current && (
                <span style={{ fontSize: '10px', color: '#6366f1', marginLeft: '1px' }}>▶</span>
              )}
            </button>
            <div className="step-body">
              <div 
                className="step-name" 
                style={{ 
                  color: current ? '#6366f1' : 'var(--text)' 
                }}
              >
                {step.description || step.skillName}
              </div>
              <div className="step-meta">{step.resourceUrl || 'Backend roadmap step'}</div>
            </div>
            <div 
              className="step-hrs" 
              style={{ 
                color: current ? '#6366f1' : 'var(--text3)',
                fontWeight: current ? '600' : 'normal'
              }}
            >
              {step.estimatedHours} hrs
            </div>
          </div>
        )})}
      </div>
    </div>
  );
};

export default Roadmap;
