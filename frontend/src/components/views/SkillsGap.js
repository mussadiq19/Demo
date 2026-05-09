import React, { useState } from 'react';

const SkillsGap = () => {
  const [selectedDepartment, setSelectedDepartment] = useState('All departments');
  
  const departments = ['All departments', 'Engineering', 'Product'];
  
  const employees = [
    {
      name: 'Rahul Mehta',
      role: 'Senior Engineer',
      missingSkills: ['LLM Fine-tuning', 'MLOps', 'Vector DBs'],
      gapScore: 82,
      gapLevel: 'Critical',
      gapColor: '#ef4444'
    },
    {
      name: 'Priya Nair',
      role: 'Product Manager',
      missingSkills: ['AI Product Mgmt', 'SQL'],
      gapScore: 64,
      gapLevel: 'High',
      gapColor: '#f97316'
    },
    {
      name: 'Vikram Iyer',
      role: 'Data Analyst',
      missingSkills: ['Python', 'dbt'],
      gapScore: 41,
      gapLevel: 'Medium',
      gapColor: '#f59e0b'
    }
  ];

  const getGapScoreColor = (score) => {
    if (score >= 80) return '#b91c1c';
    if (score >= 60) return '#c2410c';
    if (score >= 40) return '#b45309';
    return '#15803d';
  };

  return (
    <div id="view-skills">
      <div 
        className="scan-banner" 
        style={{ 
          background: 'linear-gradient(135deg,#1a1040,#0f1117)', 
          marginBottom: '2px' 
        }}
      >
        <div className="scan-icon" style={{ background: 'rgba(99,102,241,0.3)' }}>
          <span style={{ fontSize: '18px', color: '#a78bfa' }}>🧠</span>
        </div>
        <div className="scan-text">
          <div className="t1">⚡ Powered by SovAI — Future Skills Gap Analyzer</div>
          <div className="t2">131 employees · 5 departments · AI-generated gap analysis · Updated May 9</div>
        </div>
        <button 
          className="btn" 
          style={{ 
            marginLeft: 'auto', 
            background: '#6366f1', 
            color: '#fff', 
            fontSize: '11px', 
            padding: '6px 12px' 
          }}
        >
          <span style={{ fontSize: '13px' }}>📤</span>
          Upload CSV
        </button>
      </div>
      
      <div className="full-card">
        <div className="card-head">
          <div>
            <div className="card-title">Employee Skills Gap Analysis</div>
            <div className="card-sub">Sorted by gap severity</div>
          </div>
          <select 
            value={selectedDepartment}
            onChange={(e) => setSelectedDepartment(e.target.value)}
            style={{
              fontSize: '12px',
              padding: '4px 10px',
              borderRadius: '6px',
              border: '0.5px solid var(--border2)',
              color: 'var(--text)',
              background: 'var(--surface)'
            }}
          >
            {departments.map(dept => (
              <option key={dept} value={dept}>{dept}</option>
            ))}
          </select>
        </div>
        
        <div style={{ 
          display: 'grid', 
          gridTemplateColumns: '2fr 3fr 2fr 80px', 
          gap: '8px', 
          padding: '6px 0', 
          borderBottom: '0.5px solid var(--border)' 
        }}>
          <div style={{ 
            fontSize: '11px', 
            fontWeight: '600', 
            color: 'var(--text3)', 
            textTransform: 'uppercase', 
            letterSpacing: '0.5px' 
          }}>
            Employee
          </div>
          <div style={{ 
            fontSize: '11px', 
            fontWeight: '600', 
            color: 'var(--text3)', 
            textTransform: 'uppercase', 
            letterSpacing: '0.5px' 
          }}>
            Missing Skills
          </div>
          <div style={{ 
            fontSize: '11px', 
            fontWeight: '600', 
            color: 'var(--text3)', 
            textTransform: 'uppercase', 
            letterSpacing: '0.5px' 
          }}>
            Gap Score
          </div>
          <div style={{ 
            fontSize: '11px', 
            fontWeight: '600', 
            color: 'var(--text3)', 
            textTransform: 'uppercase', 
            letterSpacing: '0.5px' 
          }}>
            Action
          </div>
        </div>
        
        {employees.map((employee, index) => (
          <div 
            key={index}
            style={{ 
              display: 'grid', 
              gridTemplateColumns: '2fr 3fr 2fr 80px', 
              gap: '8px', 
              alignItems: 'center', 
              padding: '9px 0',
              borderBottom: index === employees.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <div>
              <div style={{ fontSize: '12px', fontWeight: '500' }}>{employee.name}</div>
              <div style={{ fontSize: '10px', color: 'var(--text3)' }}>{employee.role}</div>
            </div>
            <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
              {employee.missingSkills.map((skill, skillIndex) => (
                <span key={skillIndex} className="risk-category">
                  {skill}
                </span>
              ))}
            </div>
            <div>
              <div className="gap-bar-wrap" style={{ marginBottom: '4px' }}>
                <div 
                  className="gap-bar" 
                  style={{ 
                    width: `${employee.gapScore}%`, 
                    background: employee.gapColor 
                  }}
                ></div>
              </div>
              <span style={{ 
                fontSize: '11px', 
                color: getGapScoreColor(employee.gapScore), 
                fontWeight: '600' 
              }}>
                {employee.gapScore} / {employee.gapLevel}
              </span>
            </div>
            <button 
              className="btn btn-primary" 
              style={{ fontSize: '10px', padding: '4px 8px' }}
            >
              Roadmap
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SkillsGap;
