import React from 'react';

const Roadmap = () => {
  const roadmapSteps = [
    {
      name: 'Python for Data Science — Foundations',
      meta: 'Coursera · Completed Apr 20',
      hours: '14 hrs',
      completed: true,
      current: false
    },
    {
      name: 'Intro to Machine Learning with scikit-learn',
      meta: 'fast.ai · Completed Apr 29',
      hours: '18 hrs',
      completed: true,
      current: false
    },
    {
      name: 'Transformer Architecture Deep Dive',
      meta: 'Andrej Karpathy · YouTube · Completed May 5',
      hours: '8 hrs',
      completed: true,
      current: false
    },
    {
      name: 'LLM Fine-tuning with LoRA — In Progress',
      meta: 'Hugging Face Course · Started May 7 · You\'re doing great!',
      hours: '12 hrs',
      completed: false,
      current: true
    },
    {
      name: 'Vector Databases & RAG Architectures',
      meta: 'Pinecone Academy · Upcoming',
      hours: '10 hrs',
      completed: false,
      current: false
    },
    {
      name: 'MLOps with MLflow & Kubeflow',
      meta: 'DataTalks.Club · Upcoming',
      hours: '16 hrs',
      completed: false,
      current: false
    },
    {
      name: 'Capstone: Deploy an LLM microservice',
      meta: 'Internal project · Upcoming',
      hours: '8 hrs',
      completed: false,
      current: false
    }
  ];

  const completedSteps = roadmapSteps.filter(step => step.completed).length;
  const totalSteps = roadmapSteps.length;
  const progressPercentage = Math.round((completedSteps / totalSteps) * 100);

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
          <div className="t2">⚡ Powered by SovAI · Rahul Mehta · Engineering · Last updated May 9</div>
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
            <div className="card-title">AI/ML Upskilling Roadmap</div>
            <div className="card-sub">
              {completedSteps} of {totalSteps} steps complete · ~48 hrs remaining
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
        
        {roadmapSteps.map((step, index) => (
          <div 
            key={index}
            className={`roadmap-step ${
              step.current ? 'current' : ''
            } ${
              !step.completed && !step.current ? 'upcoming' : ''
            }`}
            style={{
              background: step.current ? 'rgba(99,102,241,0.03)' : 'transparent',
              borderRadius: step.current ? '8px' : '0',
              padding: step.current ? '8px 10px' : '8px 0',
              margin: step.current ? '0 -10px' : '0',
              opacity: !step.completed && !step.current ? '0.5' : '1',
              borderBottom: index === roadmapSteps.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <div 
              className={`step-check ${step.completed ? 'done' : ''}`}
              style={{ 
                borderColor: step.current ? '#6366f1' : 'var(--border2)',
                background: step.completed ? '#22c55e' : 'transparent'
              }}
            >
              {step.completed && (
                <span style={{ fontSize: '11px', color: '#fff' }}>✓</span>
              )}
              {step.current && (
                <span style={{ fontSize: '10px', color: '#6366f1', marginLeft: '1px' }}>▶</span>
              )}
            </div>
            <div className="step-body">
              <div 
                className="step-name" 
                style={{ 
                  color: step.current ? '#6366f1' : 'var(--text)' 
                }}
              >
                {step.name}
              </div>
              <div className="step-meta">{step.meta}</div>
            </div>
            <div 
              className="step-hrs" 
              style={{ 
                color: step.current ? '#6366f1' : 'var(--text3)',
                fontWeight: step.current ? '600' : 'normal'
              }}
            >
              {step.hours}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Roadmap;
