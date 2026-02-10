INSERT INTO companies (name, cnpj, business_name, website_url, is_active, company_url_logo) VALUES
('Empresa Teste 1', '12345678000199', 'Teste LTDA', 'https://sellvia.com.br', true, 'https://images.vexels.com/media/users/3/142789/isolated/preview/2bfb04ad814c4995f0c537c68db5cd0b-logotipo-do-circulo-multicolorido.png'),
('Empresa Teste 2', '12345678000198', 'Teste LTDA 2', 'https://sellvia2.com.br', true, 'https://images.vexels.com/media/users/3/142789/isolated/preview/2bfb04ad814c4995f0c537c68db5cd0b-logotipo-do-circulo-multicolorido.png');

INSERT INTO companies (name, cnpj, business_name, website_url, is_active, company_url_logo) VALUES
('Tech Inovação', '98765432000100', 'Tech Inovação S.A.', 'https://techinovacao.com.br', true, 'https://via.placeholder.com/150'),
('Varejo Global', '11222333000144', 'Varejo Global Distribuidora', 'https://varejoglobal.com', true, 'https://via.placeholder.com/150'),
('Logística Express', '44555666000177', 'LogExpress LTDA', 'https://logexpress.com.br', false, 'https://via.placeholder.com/150');

INSERT INTO users (name, username, cpf, is_active, password, created_at, updated_at, created_by, updated_by, role, company_id, email)
VALUES (
    'Admin Claus',
    'claus_admin',
    '12345678901',
    true,
    '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy',
    NOW(),
    NOW(),
    'system',
    'system',
    'SYSTEM_ADMIN',
    1,
    'clausilvaaraujo11@gmail.com'
);

INSERT INTO categories (name, description, created_at, updated_at, created_by, updated_by, company_id) VALUES
('Eletrônicos', 'Produtos tecnológicos e gadgets', NOW(), NOW(), 'system', 'system', 1),
('Escritório', 'Materiais para escritório e papelaria', NOW(), NOW(), 'system', 'system', 1),
('Alimentos', 'Produtos perecíveis e mercearia', NOW(), NOW(), 'system', 'system', 3),
('Limpeza', 'Produtos de higiene e conservação', NOW(), NOW(), 'system', 'system', 3),
('Móveis', 'Móveis para casa e jardim', NOW(), NOW(), 'system', 'system', 2);

INSERT INTO users (name, username, cpf, is_active, password, created_at, updated_at, created_by, updated_by, role, company_id, email) VALUES
-- Administradores de Empresas
('Ricardo Silva', 'ricardo_admin', '22334455667', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_ADMIN', 1, 'ricardo@techinovacao.com'),
('Fernanda Lima', 'fernanda_admin', '33445566778', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_ADMIN', 3, 'fernanda@logexpress.com'),

-- Usuários comuns de Empresas
('João Peão', 'joao_user', '44556677889', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_USER', 1, 'joao@techinovacao.com'),
('Maria Vendas', 'maria_user', '55667788990', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_USER', 2, 'maria@varejoglobal.com'),

-- Usuários SEM Empresa (Independentes ou System Admins)
('Suporte Master', 'suporte_claus', '99887766554', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'SYSTEM_ADMIN', NULL, 'suporte@sellvia.com.br'),
('Consultor Externo', 'consultor_free', '11223344556', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'SYSTEM_ADMIN', NULL, 'consultor@gmail.com');