import React, { useEffect, useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { useAiReadiness } from '../../hooks/useAiReadiness';
import { companyService } from '../../services/companyService';

const CompanyProfile = () => {
  const { user } = useAuth();
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: '',
    industry: '',
    size: '',
    location: '',
  });
  const { data: company } = useQuery({
    queryKey: ['company', user?.companyId],
    queryFn: () => companyService.getCompany(user.companyId),
    enabled: !!user?.companyId,
  });
  const { data } = useAiReadiness();

  useEffect(() => {
    if (!company) return;
    setForm({
      name: company.name || '',
      industry: company.industry || '',
      size: company.size || '',
      location: company.location || company.techStack || '',
    });
  }, [company]);

  const updateMutation = useMutation({
    mutationFn: () => companyService.updateCompany(user.companyId, form),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['company', user?.companyId] });
      queryClient.invalidateQueries({ queryKey: ['ai-readiness', user?.companyId] });
    },
  });

  const handleChange = (field) => (event) => {
    setForm((current) => ({
      ...current,
      [field]: event.target.value,
    }));
  };

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
                value={form.name}
                onChange={handleChange('name')}
                className="input"
              />
            </div>

            <div className="form-group">
              <label>Industry</label>
              <select className="input" value={form.industry} onChange={handleChange('industry')}>
                <option value={form.industry}>{form.industry || 'Not provided'}</option>
              </select>
            </div>

            <div className="form-group">
              <label>Company Size</label>
              <select className="input" value={form.size} onChange={handleChange('size')}>
                <option value={form.size}>{form.size || 'Not provided'}</option>
              </select>
            </div>

            <div className="form-group">
              <label>Location</label>
              <input
                type="text"
                value={form.location}
                onChange={handleChange('location')}
                className="input"
              />
            </div>
          </div>

          <div className="profile-section">
            <div className="section-title">AI Readiness</div>

            <div className="readiness-card">
              <div className="readiness-score">{data?.overallScore ?? 0}%</div>
              <div className="readiness-label">
                AI Transformation Readiness
              </div>
            </div>

            <div className="stat-list">
              <div className="stat-item">
                <span>Automation Coverage</span>
                <strong>{data?.automationCoverage ?? 0}%</strong>
              </div>

              <div className="stat-item">
                <span>AI Adoption</span>
                <strong>{data?.aiAdoption ?? 0}%</strong>
              </div>

              <div className="stat-item">
                <span>Workforce Preparedness</span>
                <strong>{data?.workforcePreparedness ?? 0}%</strong>
              </div>

              <div className="stat-item">
                <span>Risk Exposure</span>
                <strong>{data?.riskExposure ?? ''}</strong>
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
          <button
            className="primary-btn"
            onClick={() => updateMutation.mutate()}
            disabled={!user?.companyId || updateMutation.isPending}
          >
            Save Changes
          </button>
          <button className="secondary-btn">Cancel</button>
        </div>
      </div>
    </div>
  );
};

export default CompanyProfile;
