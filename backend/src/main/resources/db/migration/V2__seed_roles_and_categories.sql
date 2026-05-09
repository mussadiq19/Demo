INSERT INTO roles (name)
VALUES ('ADMIN'), ('COMPANY_ADMIN'), ('EMPLOYEE')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO risk_categories (name, icon)
VALUES
  ('Supply Chain', 'truck'),
  ('Technology', 'cpu'),
  ('Market', 'chart-line')
ON DUPLICATE KEY UPDATE name = VALUES(name);
