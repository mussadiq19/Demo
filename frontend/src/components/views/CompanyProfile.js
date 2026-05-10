import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { companyService } from '../../services/companyService';

const CompanyProfile = () => {
  const { user } = useAuth();
  const { data: company } = useQuery({
    queryKey: ['company', user?.companyId],
    queryFn: () => companyService.getCompany(user.companyId),
    enabled: !!user?.companyId,
  });

  return (
    <div className="profile-page">
      <div className="card">
        <div className="card-head">
          <div>
            <div className="card-title">Company Profile</div>
            <div className="card-sub">
              Manage your organization details and AI readiness profile.
            </div>
          </div>
        </div>

        <div className="profile-grid">
          <div className="profile-section">
            <div className="section-title">Organization Details</div>

            <div className="form-group">
              <label>Company Name</label>
              <input
                type="text"
                value={company?.name || ''}
                readOnly
                className="input"
              />
            </div>

            <div className="form-group">
              <label>Industry</label>
              <select className="input" value={company?.industry || ''} onChange={() => {}}>
                <option value={company?.industry || ''}>{company?.industry || 'Not provided'}</option>
              </select>
            </div>

            <div className="form-group">
              <label>Company Size</label>
              <select className="input" value={company?.size || ''} onChange={() => {}}>
                <option value={company?.size || ''}>{company?.size || 'Not provided'}</option>
              </select>
            </div>

            <div className="form-group">
              <label>Location</label>
              <input
                type="text"
                value={company?.techStack || ''}
                readOnly
                className="input"
              />
            </div>
          </div>

          <div className="profile-section">
            <div className="section-title">AI Readiness</div>

            <div className="readiness-card">
              <div className="readiness-score">82%</div>
              <div className="readiness-label">
                AI Transformation Readiness
              </div>
            </div>

            <div className="stat-list">
              <div className="stat-item">
                <span>Automation Coverage</span>
                <strong>68%</strong>
              </div>

              <div className="stat-item">
                <span>AI Adoption</span>
                <strong>74%</strong>
              </div>

              <div className="stat-item">
                <span>Workforce Preparedness</span>
                <strong>81%</strong>
              </div>

              <div className="stat-item">
                <span>Risk Exposure</span>
                <strong>Medium</strong>
              </div>
            </div>
          </div>
        </div>

        <div className="profile-section">
          <div className="section-title">Company Description</div>

          <textarea
            className="input textarea"
            rows="6"
            value={company?.supplyChainInfo || ''}
            readOnly
          ></textarea>
        </div>

        <div className="profile-actions">
          <button className="primary-btn">Save Changes</button>
          <button className="secondary-btn">Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default CompanyProfile;
