import React, { useState } from 'react';

const UploadSkills = () => {
  const [dragActive, setDragActive] = useState(false);
  const [uploadedFiles, setUploadedFiles] = useState([]);

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === "dragenter" || e.type === "dragover") {
      setDragActive(true);
    } else if (e.type === "dragleave") {
      setDragActive(false);
    }
  };

  const handleDrop = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);
    
    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFiles(e.dataTransfer.files);
    }
  };

  const handleChange = (e) => {
    e.preventDefault();
    if (e.target.files && e.target.files[0]) {
      handleFiles(e.target.files);
    }
  };

  const handleFiles = (files) => {
    const newFiles = Array.from(files).map(file => ({
      name: file.name,
      size: (file.size / 1024).toFixed(2) + ' KB',
      type: file.type,
      uploadDate: new Date().toLocaleDateString()
    }));
    setUploadedFiles(prev => [...prev, ...newFiles]);
  };

  const removeFile = (index) => {
    setUploadedFiles(prev => prev.filter((_, i) => i !== index));
  };

  return (
    <div className="upload-page">
      <div className="upload-banner">
        <div className="upload-icon">
          <span style={{ fontSize: '24px', color: '#818cf8' }}>📊</span>
        </div>
        <div className="upload-text">
          <div className="t1">Upload Employee Skills Data</div>
          <div className="t2">
            CSV or Excel files · Max 10MB · AI-powered analysis will begin automatically
          </div>
        </div>
      </div>

      <div className="upload-section">
        <div className="card">
          <div className="card-head">
            <div>
              <div className="card-title">File Upload</div>
              <div className="card-sub">Drag and drop or click to browse</div>
            </div>
          </div>

          <div 
            className={`upload-area ${dragActive ? 'active' : ''}`}
            onDragEnter={handleDrag}
            onDragLeave={handleDrag}
            onDragOver={handleDrag}
            onDrop={handleDrop}
          >
            <div className="upload-content">
              <div className="upload-icon-large">
                <span style={{ fontSize: '48px', color: '#6366f1' }}>📤</span>
              </div>
              <div className="upload-title">
                {dragActive ? 'Drop files here' : 'Drag & drop files here'}
              </div>
              <div className="upload-subtitle">or</div>
              <label htmlFor="file-upload" className="upload-btn">
                Browse Files
              </label>
              <input
                id="file-upload"
                type="file"
                multiple
                accept=".csv,.xlsx,.xls"
                onChange={handleChange}
                style={{ display: 'none' }}
              />
              <div className="upload-info">
                Supported formats: CSV, Excel (.xlsx, .xls)
              </div>
            </div>
          </div>

          {uploadedFiles.length > 0 && (
            <div className="uploaded-files">
              <div className="section-title">Uploaded Files</div>
              {uploadedFiles.map((file, index) => (
                <div key={index} className="file-item">
                  <div className="file-info">
                    <div className="file-icon">
                      <span style={{ fontSize: '16px', color: '#6366f1' }}>📄</span>
                    </div>
                    <div className="file-details">
                      <div className="file-name">{file.name}</div>
                      <div className="file-meta">{file.size} • {file.uploadDate}</div>
                    </div>
                  </div>
                  <button 
                    className="remove-btn"
                    onClick={() => removeFile(index)}
                  >
                    <span style={{ fontSize: '14px' }}>✕</span>
                  </button>
                </div>
              ))}
            </div>
          )}

          <div className="template-section">
            <div className="section-title">Need a template?</div>
            <div className="template-options">
              <button className="template-btn">
                <span style={{ fontSize: '16px', marginRight: '8px' }}>📋</span>
                Download CSV Template
              </button>
              <button className="template-btn">
                <span style={{ fontSize: '16px', marginRight: '8px' }}>📊</span>
                Download Excel Template
              </button>
            </div>
          </div>

          <div className="upload-actions">
            <button className="primary-btn" disabled={uploadedFiles.length === 0}>
              <span style={{ marginRight: '6px' }}>⚡</span>
              Start AI Analysis
            </button>
            <button className="secondary-btn">Clear All</button>
          </div>
        </div>
      </div>

      <div className="help-section">
        <div className="card">
          <div className="card-head">
            <div>
              <div className="card-title">Upload Guidelines</div>
              <div className="card-sub">Ensure your data follows these requirements</div>
            </div>
          </div>
          
          <div className="guidelines">
            <div className="guideline-item">
              <div className="guideline-icon">✓</div>
              <div className="guideline-text">
                <strong>Required Columns:</strong> Employee Name, Department, Current Skills, Skill Level
              </div>
            </div>
            <div className="guideline-item">
              <div className="guideline-icon">✓</div>
              <div className="guideline-text">
                <strong>Optional Columns:</strong> Email, Role, Years of Experience, Certifications
              </div>
            </div>
            <div className="guideline-item">
              <div className="guideline-icon">✓</div>
              <div className="guideline-text">
                <strong>Skill Levels:</strong> Beginner, Intermediate, Advanced, Expert
              </div>
            </div>
            <div className="guideline-item">
              <div className="guideline-icon">✓</div>
              <div className="guideline-text">
                <strong>File Size:</strong> Maximum 10MB per file
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UploadSkills;
