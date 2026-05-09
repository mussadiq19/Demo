import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import DashboardOverview from './views/DashboardOverview';
import RiskScanner from './views/RiskScanner';
import SkillsGap from './views/SkillsGap';
import Roadmap from './views/Roadmap';
import CompanyProfile from './views/CompanyProfile';
import UploadSkills from './views/UploadSkills';

const Dashboard = () => {
  const [activeView, setActiveView] = useState('dashboard');
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  /**
   * Handle logout
   */
  const handleLogout = async () => {
    await logout();
    navigate('/login', { replace: true });
  };

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', section: 'Platform' },
    { id: 'risks', label: 'Risk Scanner', section: 'Platform', badge: '3' },
    { id: 'skills', label: 'Skills Gap', section: 'Workforce' },
    { id: 'roadmap', label: 'My Roadmap', section: 'Workforce' },
    { id: 'upload', label: 'Upload Skills', section: 'Workforce' },
    { id: 'profile', label: 'Company Profile', section: 'Settings' }
  ];

  const getPageTitle = () => {
    const titles = {
      dashboard: 'Dashboard Overview',
      risks: 'AI Risk Scanner',
      skills: 'Future Skills Gap Analyzer',
      roadmap: 'My Growth Roadmap',
      upload: 'Upload Skills Data',
      profile: 'Company Profile'
    };
    return titles[activeView] || 'Dashboard';
  };

  const renderView = () => {
    switch (activeView) {
      case 'dashboard':
        return <DashboardOverview />;
      case 'risks':
        return <RiskScanner />;
      case 'skills':
        return <SkillsGap />;
      case 'roadmap':
        return <Roadmap />;
      case 'upload':
        return <UploadSkills />;
      case 'profile':
        return <CompanyProfile />;
      default:
        return <DashboardOverview />;
    }
  };

  const groupedNavItems = navItems.reduce((acc, item) => {
    if (!acc[item.section]) {
      acc[item.section] = [];
    }
    acc[item.section].push(item);
    return acc;
  }, {});

  return (
    <div className="shell">
      <nav className="nav">
        <div className="nav-brand">
          <div className="nav-logo">Sov<span>AI</span></div>
          <div className="nav-sub">Sovereign Intelligence</div>
        </div>
        
        {Object.entries(groupedNavItems).map(([section, items]) => (
          <div key={section}>
            <div className="nav-section">{section}</div>
            {items.map(item => (
              <div
                key={item.id}
                className={`nav-item ${activeView === item.id ? 'active' : ''}`}
                onClick={() => setActiveView(item.id)}
              >
                <div className="nav-dot"></div>
                {item.label}
                {item.badge && <span className="nav-badge">{item.badge}</span>}
              </div>
            ))}
          </div>
        ))}
        
        <div className="nav-footer">
          <div className="nav-user">
            <div className="nav-avatar">
              {user?.userId ? String(user.userId).charAt(0).toUpperCase() : 'U'}
            </div>
            <div>
              <div className="nav-username">User ID: {user?.userId}</div>
              <div className="nav-role">{user?.role || 'User'}</div>
            </div>
          </div>
          <button
            className="nav-logout-btn"
            onClick={handleLogout}
            title="Logout"
          >
            Logout
          </button>
        </div>
      </nav>

      <div className="main">
        <div className="topbar">
          <div className="page-title">{getPageTitle()}</div>
          <div className="topbar-right">
            <div className="sovai-badge">⚡ Powered by SovAI</div>
            <div className="notif-btn">
              <span style={{ fontSize: '15px', color: 'var(--text2)' }}>🔔</span>
              <div className="notif-dot"></div>
            </div>
            <div className="nav-avatar" style={{ width: '30px', height: '30px', fontSize: '11px' }}>
              AS
            </div>
          </div>
        </div>

        <div className="content" id="main-content">
          {renderView()}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
