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

INSERT INTO categories (name, description, created_at, updated_at, created_by, updated_by, company_id) VALUES
('Processadores', 'CPUs de última geração para desktops e notebooks', NOW(), NOW(), 'system', 'system', 1),
('Placas de Vídeo', 'GPUs para jogos e renderização profissional', NOW(), NOW(), 'system', 'system', 1),
('Memória RAM', 'Módulos de memória DDR4 e DDR5', NOW(), NOW(), 'system', 'system', 1),
('Armazenamento SSD', 'Unidades de estado sólido de alta velocidade', NOW(), NOW(), 'system', 'system', 1),
('Monitores 4K', 'Displays de alta resolução para produtividade', NOW(), NOW(), 'system', 'system', 1),
('Teclados Mecânicos', 'Periféricos de entrada de alto desempenho', NOW(), NOW(), 'system', 'system', 1),

('Licenças de SO', 'Sistemas operacionais e licenças corporativas', NOW(), NOW(), 'system', 'system', 1),
('Antivírus e Segurança', 'Suítes de proteção digital e firewalls', NOW(), NOW(), 'system', 'system', 1),
('Softwares de Edição', 'Ferramentas para design e edição de vídeo', NOW(), NOW(), 'system', 'system', 1),
('Cloud Storage', 'Serviços de armazenamento e backup em nuvem', NOW(), NOW(), 'system', 'system', 1),

('Roteadores Wi-Fi 6', 'Equipamentos de rede sem fio de alta performance', NOW(), NOW(), 'system', 'system', 1),
('Switches Gerenciáveis', 'Hardware para infraestrutura de rede local', NOW(), NOW(), 'system', 'system', 1),
('Cabos e Conectividade', 'Cabos de rede, adaptadores e fibra óptica', NOW(), NOW(), 'system', 'system', 1),

('Headsets Gamer', 'Equipamentos de áudio com microfone integrado', NOW(), NOW(), 'system', 'system', 1),
('Caixas de Som Bluetooth', 'Dispositivos de áudio portáteis', NOW(), NOW(), 'system', 'system', 1),
('Webcams Full HD', 'Câmeras para videoconferência e streaming', NOW(), NOW(), 'system', 'system', 1),

('Lâmpadas Inteligentes', 'Iluminação controlada por aplicativo', NOW(), NOW(), 'system', 'system', 1),
('Assistentes Virtuais', 'Dispositivos com Alexa e Google Assistant', NOW(), NOW(), 'system', 'system', 1),
('Câmeras de Segurança IP', 'Monitoramento residencial e empresarial', NOW(), NOW(), 'system', 'system', 1),

('Toners e Tintas', 'Suprimentos para impressão a laser e jato', NOW(), NOW(), 'system', 'system', 1),
('Papéis Especiais', 'Papéis fotográficos e para documentos', NOW(), NOW(), 'system', 'system', 1),
('Baterias e Pilhas', 'Fontes de energia portáteis', NOW(), NOW(), 'system', 'system', 1);

INSERT INTO users (name, username, cpf, is_active, password, created_at, updated_at, created_by, updated_by, role, company_id, email) VALUES
('Ricardo Silva', 'ricardo_admin', '22334455667', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_ADMIN', 1, 'ricardo@techinovacao.com'),
('Fernanda Lima', 'fernanda_admin', '33445566778', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_ADMIN', 3, 'fernanda@logexpress.com'),

('João Peão', 'joao_user', '44556677889', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_USER', 1, 'joao@techinovacao.com'),
('Maria Vendas', 'maria_user', '55667788990', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'COMPANY_USER', 2, 'maria@varejoglobal.com'),

('Suporte Master', 'suporte_claus', '99887766554', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'SYSTEM_ADMIN', NULL, 'suporte@sellvia.com.br'),
('Consultor Externo', 'consultor_free', '11223344556', true, '$2a$10$eFmiwr46VX5dp4A6uiWPVu.qSDues5Aw29H8MdvBmpm.dx2LsUVCy', NOW(), NOW(), 'system', 'system', 'SYSTEM_ADMIN', NULL, 'consultor@gmail.com');

INSERT INTO saleable_items (id, name, description, price, production_cost, image_url, company_id, status, created_at, created_by, item_type)
VALUES (1, 'Smartphone Galaxy S24', 'Smartphone de última geração com IA', 5499.00, 3200.00, 'https://link.com/foto1.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT');

INSERT INTO products (id, sku, stock_quantity, type)
VALUES (1, 'SAM-S24-128GB', 50, 'PHYSICAL');

INSERT INTO saleable_items (id, name, description, price, production_cost, image_url, company_id, status, created_at, created_by, item_type)
VALUES (2, 'Fone Bluetooth Noise Cancelling', 'Fone de ouvido com cancelamento de ruído ativo', 899.90, 450.00, 'https://link.com/foto2.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT');

INSERT INTO products (id, sku, stock_quantity, type)
VALUES (2, 'AUD-NOISE-900', 120, 'PHYSICAL');

INSERT INTO saleable_items (id, name, description, price, production_cost, image_url, company_id, status, created_at, created_by, item_type)
VALUES (3, 'Curso de Kotlin Avançado', 'Acesso vitalício ao curso de Kotlin para backend', 299.00, 50.00, 'https://link.com/foto3.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT');

INSERT INTO products (id, sku, stock_quantity, type)
VALUES (3, 'EDU-KOTLIN-AV', NULL, 'DIGITAL');