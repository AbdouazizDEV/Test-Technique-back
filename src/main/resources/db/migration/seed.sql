USE article_manager;

-- Admin : password = Admin@2025
INSERT INTO users (name, email, password, role) VALUES
('Administrateur GS1', 'admin@gs1sn.com',
 '$2a$10$HOh1g2BYtcMckF.XvOnIGudHvFHkE9eBzLnkA58/Wxuq/41w0hmuy',
 'ROLE_ADMIN');

-- Membres de test : password = password123
INSERT INTO users (name, email, password, role) VALUES
('Mamadou Diallo', 'mamadou@gs1sn.com',
 '$2a$10$vbSRwkuJ239XsYf5UwrraetJo2ADJYH8maFvtQrm2iJlF3tJdE51.', 'ROLE_MEMBER'),
('Fatou Ndiaye', 'fatou@gs1sn.com',
 '$2a$10$Ixo.hZsaGaGiqnfhx7VB8Oj3Vv/udj5OidiRxuxYriOZrdKm1wCwO', 'ROLE_MEMBER');

-- Articles de test
INSERT INTO articles (title, content, author_id) VALUES
('Introduction au Clean Code', 'Le Clean Code est une philosophie...', 2),
('Les principes SOLID', 'SOLID est un acronyme représentant...', 2),
('Architecture Hexagonale', 'L hexagonale permet de découpler...', 3),
('Le refactoring', 'Le refactoring est une technique...', 3),
('Le design pattern', 'Le design pattern est une technique...', 2);
