import React, { useState } from 'react';
import { useSkillsGap } from '../../hooks/useSkillsGap';

const SkillsGap = () => {
  const [selectedDepartment, setSelectedDepartment] = useState('All departments');
  const { data: gapsData, isLoading } = useSkillsGap();

  const departments = ['All departments', ...(gapsData?.byDepartment || []).map((dept) => dept.name)];
  const rows = (gapsData?.byDepartment || []).filter((dept) =>
    selectedDepartment === 'All departments' || dept.name === selectedDepartment
  );
  const employeeRows = gapsData?.employeeGaps || [];

  const getGapScoreColor = (score) => {
    if (score >= 80) return '#b91c1c';
    if (score >= 60) return '#c2410c';
    if (score >= 40) return '#b45309';
    return '#15803d';
  };

  const getGapLevel = (score) => {
    if (score >= 80) return 'Critical';
    if (score >= 60) return 'High';
    if (score >= 40) return 'Medium';
    return 'Low';
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
          <div className="t1">⚡ Powered by Foresight AI — Future Skills Gap Analyzer</div>
          <div className="t2">{gapsData?.overallGapPercentage ?? 0}% overall gap · {rows.length} departments · AI-generated gap analysis</div>
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
            <div className="card-title">Department Skills Gap Analysis</div>
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
            Department
          </div>
          <div style={{ 
            fontSize: '11px', 
            fontWeight: '600', 
            color: 'var(--text3)', 
            textTransform: 'uppercase', 
            letterSpacing: '0.5px' 
          }}>
            Staff
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
        
        {isLoading ? (
          <div style={{ padding: '9px 0', fontSize: '12px', color: 'var(--text3)' }}>Loading skills gaps...</div>
        ) : rows.map((department, index) => (
          <div 
            key={department.name}
            style={{ 
              display: 'grid', 
              gridTemplateColumns: '2fr 3fr 2fr 80px', 
              gap: '8px', 
              alignItems: 'center', 
              padding: '9px 0',
              borderBottom: index === rows.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <div>
              <div style={{ fontSize: '12px', fontWeight: '500' }}>{department.name}</div>
              <div style={{ fontSize: '10px', color: 'var(--text3)' }}>{department.staffCount} employees</div>
            </div>
            <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
              <span className="risk-category">
                {department.staffCount} staff
              </span>
            </div>
            <div>
              <div className="gap-bar-wrap" style={{ marginBottom: '4px' }}>
                <div 
                  className="gap-bar" 
                  style={{ 
                    width: `${department.gapPercentage}%`, 
                    background: getGapScoreColor(department.gapPercentage) 
                  }}
                ></div>
              </div>
              <span style={{ 
                fontSize: '11px', 
                color: getGapScoreColor(department.gapPercentage), 
                fontWeight: '600' 
              }}>
                {department.gapPercentage} / {getGapLevel(department.gapPercentage)}
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
      
      <div className="full-card" style={{ marginTop: '8px' }}>
        <div className="card-head">
          <div>
            <div className="card-title">Individual Skills Gap</div>
            <div className="card-sub">Missing skills from employee gap analysis</div>
          </div>
        </div>
        
        {isLoading ? (
          <div style={{ padding: '9px 0', fontSize: '12px', color: 'var(--text3)' }}>Loading employee gaps...</div>
        ) : employeeRows.length === 0 ? (
          <div style={{ padding: '9px 0', fontSize: '12px', color: 'var(--text3)' }}>No employee gap details yet</div>
        ) : employeeRows.map((employeeGap, index) => (
          <div 
            key={employeeGap.userId ?? index}
            style={{ 
              display: 'grid', 
              gridTemplateColumns: '2fr 3fr 2fr 80px', 
              gap: '8px', 
              alignItems: 'center', 
              padding: '9px 0',
              borderBottom: index === employeeRows.length - 1 ? 'none' : '0.5px solid var(--border)'
            }}
          >
            <div>
              <div style={{ fontSize: '12px', fontWeight: '500' }}>Employee {employeeGap.userId}</div>
              <div style={{ fontSize: '10px', color: 'var(--text3)' }}>{employeeGap.riskLevel || 'Gap'} risk</div>
            </div>
            <div style={{ display: 'flex', gap: '4px', flexWrap: 'wrap' }}>
              {(employeeGap.missingSkills || []).map((skill) => (
                <span key={skill} className="risk-category">
                  {skill}
                </span>
              ))}
            </div>
            <div style={{ fontSize: '11px', color: 'var(--text3)' }}>
              {employeeGap.recommendation}
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
