INSERT INTO companies (name, cnpj, business_name, website_url, is_active, company_url_logo)
VALUES ('Empresa Teste', '12345678000199', 'Teste LTDA', 'https://sellvia.com.br', true, 'https://images.vexels.com/media/users/3/142789/isolated/preview/2bfb04ad814c4995f0c537c68db5cd0b-logotipo-do-circulo-multicolorido.png');

INSERT INTO users (name, username, cpf, is_active, password, created_at, updated_at, created_by, updated_by, role, company_id)
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
    1
);