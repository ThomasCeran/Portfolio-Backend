INSERT INTO role (name)
VALUES ('ADMIN')
ON CONFLICT (name) DO NOTHING;

INSERT INTO app_user (username, email, password, role_id)
SELECT 'Thomas', 'thomas.ceran.dev@gmail.com',
       '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36N/dMT7w7P4Qh/6R9Fvum', -- password: "admin123"
       r.id
FROM role r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM app_user WHERE email = 'thomas.ceran.dev@gmail.com');
