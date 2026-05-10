import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import AppLayout from './AppLayout';
import DashboardOverview from './views/DashboardOverview';
import RiskScanner from './views/RiskScanner';
import SkillsGap from './views/SkillsGap';
import Roadmap from './views/Roadmap';
import CompanyProfile from './views/CompanyProfile';
import UploadSkills from './views/UploadSkills';
import { useRecentRisks } from '../hooks/useRecentRisks';

const Dashboard = () => {
  const [activeView, setActiveView] = useState('dashboard');
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const { data: recentRisks } = useRecentRisks();

  /**
   * Handle logout
   */
  const handleLogout = async () => {
    await logout();
    navigate('/login', { replace: true });
  };

  const navItems = [
    { id: 'dashboard', label: 'Dashboard', section: 'Platform' },
    { id: 'risks', label: 'Risk Scanner', section: 'Platform', badge: recentRisks?.totalElements ? String(recentRisks.totalElements) : undefined },
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


  return (
    <AppLayout
      pageTitle={getPageTitle()}
      navItems={navItems}
      activeView={activeView}
      setActiveView={setActiveView}
      user={user}
      onLogout={handleLogout}
    >
      <div id="main-content">
        {renderView()}
      </div>
    </AppLayout>
  );
};

export default Dashboard;
