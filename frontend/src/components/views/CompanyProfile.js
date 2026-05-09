import React from 'react';

const CompanyProfile = () => {
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
                defaultValue="SovAI Technologies"
                className="input"
              />
            </div>

            <div className="form-group">
              <label>Industry</label>
              <select className="input">
                <option>Technology</option>
                <option>Healthcare</option>
                <option>Finance</option>
                <option>Education</option>
              </select>
            </div>

            <div className="form-group">
              <label>Company Size</label>
              <select className="input">
                <option>1 - 10 Employees</option>
                <option>11 - 50 Employees</option>
                <option>51 - 200 Employees</option>
                <option>201 - 1000 Employees</option>
              </select>
            </div>

            <div className="form-group">
              <label>Location</label>
              <input
                type="text"
                defaultValue="Mumbai, India"
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
            defaultValue="SovAI Technologies is focused on helping organizations adapt to the future of AI-driven workforces through advanced analytics, automation strategies, and workforce transformation solutions."
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