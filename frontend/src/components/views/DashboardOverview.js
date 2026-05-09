import React from 'react';

const DashboardOverview = () => {
  const statCards = [
    {
      label: 'Critical Risks',
      value: '3',
      delta: '↑ 2 since last scan',
      deltaClass: 'up',
      color: 'var(--risk-crit)',
      fillWidth: '30%',
      fillColor: 'var(--risk-crit)'
    },
    {
      label: 'High Risks',
      value: '11',
      delta: '↑ 1 new this week',
      deltaClass: 'up',
      color: 'var(--risk-high)',
      fillWidth: '55%',
      fillColor: 'var(--risk-high)'
    },
    {
      label: 'Skills Gaps',
      value: '68%',
      delta: '↓ 4% from last month',
      deltaClass: 'down',
      color: '#6366f1',
      fillWidth: '68%',
      fillColor: '#6366f1'
    },
    {
      label: 'Roadmap Progress',
      value: '42%',
      delta: '↑ on track',
      deltaClass: 'down',
      color: '#22c55e',
      fillWidth: '42%',
      fillColor: '#22c55e'
    }
  ];

  const riskAlerts = [
    {
      severity: 'crit',
      severityLabel: 'Critical',
      title: 'TSMC supply disruption — Q3 chip shortage risk',
      time: '2h ago'
    },
    {
      severity: 'high',
      severityLabel: 'High',
      title: 'React 19 deprecation of legacy APIs affecting codebase',
      time: '5h ago'
    },
    {
      severity: 'high',
      severityLabel: 'High',
      title: 'New DPDP Act compliance requirements — India',
      time: '8h ago'
    },
    {
      severity: 'med',
      severityLabel: 'Medium',
      title: 'Competitor launched feature set overlap — pricing pressure',
      time: '12h ago'
    }
  ];

  const skillsGapData = [
    { dept: 'Engineering', gap: 78, staff: 42, color: '#ef4444' },
    { dept: 'Product', gap: 65, staff: 18, color: '#f97316' },
    { dept: 'Marketing', gap: 52, staff: 24, color: '#f59e0b' },
    { dept: 'Sales', gap: 38, staff: 31, color: '#22c55e' },
    { dept: 'Operations', gap: 28, staff: 16, color: '#22c55e' }
  ];

  return (
    <div id="view-dashboard">
      <div className="scan-banner" style={{ marginBottom: '2px' }}>
        <div className="scan-icon">
          <span style={{ fontSize: '18px', color: '#818cf8' }}>📡</span>
        </div>
        <div className="scan-text">
          <div className="t1">
            <span className="pulse-dot"></span>
            Live threat monitoring active
          </div>
          <div className="t2">
            Last scan completed 4 minutes ago · 847 signals processed
          </div>
        </div>
        <button 
          className="btn btn-scan" 
          style={{ marginLeft: 'auto', fontSize: '11px', padding: '6px 12px' }}
        >
          <span style={{ fontSize: '13px' }}>📡</span>
          Run AI Scan
        </button>
      </div>

      <div className="grid-4">
        {statCards.map((card, index) => (
          <div key={index} className="stat-card">
            <div className="stat-label">{card.label}</div>
            <div className="stat-val" style={{ color: card.color }}>
              {card.value}
            </div>
            <div className={`stat-delta ${card.deltaClass}`}>
              {card.delta}
            </div>
            <div className="severity-bar">
              <div 
                className="severity-fill" 
                style={{ width: card.fillWidth, background: card.fillColor }}
              ></div>
            </div>
          </div>
        ))}
      </div>

      <div className="grid-2">
        <div className="card">
          <div className="card-head">
            <div>
              <div className="card-title">Recent Risk Alerts</div>
              <div className="card-sub">Past 24 hours</div>
            </div>
            <div className="sovai-badge">SovAI ✦</div>
          </div>
          {riskAlerts.map((risk, index) => (
            <div key={index} className="risk-row">
              <span className={`severity-pill ${risk.severity}`}>
                {risk.severityLabel}
              </span>
              <span className="risk-title">{risk.title}</span>
              <span className="risk-time">{risk.time}</span>
            </div>
          ))}
          <div className="ai-insight">
            <div className="ai-insight-label">⚡ SovAI Insight</div>
            <div className="ai-insight-text">
              Supply chain risk elevated. Recommend qualifying 2 alternative chip vendors before Q2 close. Your team has the procurement skills to act on this today.
            </div>
          </div>
        </div>

        <div className="card">
          <div className="card-head">
            <div>
              <div className="card-title">Skills Gap by Department</div>
              <div className="card-sub">% employees with identified gaps</div>
            </div>
          </div>
          {skillsGapData.map((dept, index) => (
            <div key={index} className="skills-row">
              <div className="dept-name">{dept.dept}</div>
              <div className="gap-bar-wrap">
                <div 
                  className="gap-bar" 
                  style={{ width: `${dept.gap}%`, background: dept.color }}
                ></div>
              </div>
              <div className="gap-score">{dept.gap}%</div>
              <div className="emp-count">{dept.staff} staff</div>
            </div>
          ))}
          <div className="ai-insight" style={{ marginTop: '8px' }}>
            <div className="ai-insight-label">⚡ SovAI Insight</div>
            <div className="ai-insight-text">
              Engineering team shows high AI/ML skill gaps aligned with your tech roadmap. 12 tailored roadmaps ready to generate.
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardOverview;
