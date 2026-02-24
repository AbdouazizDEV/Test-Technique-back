-- PostgreSQL Seed Data for Article Manager (Railway)
-- Note: Railway crée déjà la base de données, donc on ne fait que insérer les données

-- Admin : password = Admin@2025
INSERT INTO users (name, email, password, role) VALUES
('Administrateur GS1', 'admin@gs1sn.com',
 '$2a$10$HOh1g2BYtcMckF.XvOnIGudHvFHkE9eBzLnkA58/Wxuq/41w0hmuy',
 'ROLE_ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Membres de test : password = password123
INSERT INTO users (name, email, password, role) VALUES
('Mamadou Diallo', 'mamadou@gs1sn.com',
 '$2a$10$vbSRwkuJ239XsYf5UwrraetJo2ADJYH8maFvtQrm2iJlF3tJdE51.', 'ROLE_MEMBER'),
('Fatou Ndiaye', 'fatou@gs1sn.com',
 '$2a$10$Ixo.hZsaGaGiqnfhx7VB8Oj3Vv/udj5OidiRxuxYriOZrdKm1wCwO', 'ROLE_MEMBER')
ON CONFLICT (email) DO NOTHING;

-- Articles de test
INSERT INTO articles (title, content, author_id) VALUES
('Introduction au Clean Code', 'Le Clean Code est une philosophie de développement qui privilégie la lisibilité, la maintenabilité et la simplicité du code source.', 
 (SELECT id FROM users WHERE email = 'mamadou@gs1sn.com' LIMIT 1)),
('Les principes SOLID', 'SOLID est un acronyme représentant cinq principes de conception orientée objet qui visent à produire des logiciels plus maintenables et extensibles.', 
 (SELECT id FROM users WHERE email = 'mamadou@gs1sn.com' LIMIT 1)),
('Architecture Hexagonale', 'L''architecture hexagonale permet de découpler la logique métier de l''infrastructure, facilitant les tests et l''évolution du système.', 
 (SELECT id FROM users WHERE email = 'fatou@gs1sn.com' LIMIT 1))
ON CONFLICT DO NOTHING;
