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

INSERT INTO saleable_items (id, name, description, price, production_cost, image_url, company_id, status, created_at, created_by, item_type, category_id) VALUES
(4, 'Processador AMD Ryzen 9 7950X', 'Processador de alta performance com 16 núcleos', 3899.00, 2100.00, 'https://link.com/ryzen9.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 1),
(5, 'Placa de Vídeo RTX 4080 Super', 'GPU para jogos em 4K e Ray Tracing', 7499.90, 4800.00, 'https://link.com/rtx4080.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 1),
(6, 'Memória RAM DDR5 32GB (2x16)', 'Módulos de alta velocidade 6000MHz', 950.00, 420.00, 'https://link.com/ram32gb.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 1),
(7, 'SSD NVMe 2TB Gen4', 'Armazenamento ultra rápido de 7500MB/s', 1100.00, 580.00, 'https://link.com/ssd2tb.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 2),
(8, 'Monitor Gamer 27" 4K 144Hz', 'Display IPS com cores precisas e alta taxa de atualização', 3200.00, 1950.00, 'https://link.com/monitor4k.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 2),
(9, 'Teclado Mecânico RGB Hot-swap', 'Switch Blue com retroiluminação customizável', 450.00, 180.00, 'https://link.com/teclado.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 3),
(10, 'Windows 11 Pro - Licença Digital', 'Chave de ativação vitalícia para Windows 11 Pro', 159.00, 40.00, 'https://link.com/win11.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 4),
(11, 'Antivírus Total Security (1 Ano)', 'Proteção completa contra malwares e ransomware', 89.90, 15.00, 'https://link.com/antivirus.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 4),
(12, 'Roteador Wi-Fi 6 AX3000', 'Cobertura ampliada e conexão para múltiplos dispositivos', 580.00, 290.00, 'https://link.com/router6.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 5),
(13, 'Headset Gamer 7.1 Surround', 'Áudio imersivo com cancelamento de ruído no microfone', 620.00, 240.00, 'https://link.com/headset.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 3),
(14, 'Lâmpada Inteligente RGB Wi-Fi', 'Controle por voz e aplicativo (16 milhões de cores)', 75.00, 28.00, 'https://link.com/smartbulb.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 6),
(15, 'Webcam 4K Ultra HD', 'Ideal para streaming e videoconferência profissional', 890.00, 410.00, 'https://link.com/webcam4k.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 7),
(16, 'Laptop Workstation Pro 16"', 'Equipado com i9 e 64GB de RAM para engenharia', 12500.00, 8200.00, 'https://link.com/laptop.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', 8),
(17, 'Mouse Wireless Ergonômico', 'Sensor de alta precisão 25k DPI', 399.00, 150.00, 'https://link.com/mouse.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(18, 'HD Externo 4TB Rugged', 'Proteção contra quedas e criptografia de hardware', 850.00, 460.00, 'https://link.com/hd4tb.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(19, 'Microfone Condensador USB', 'Qualidade de estúdio para podcasts e lives', 720.00, 310.00, 'https://link.com/mic.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(20, 'Nobreak 1500VA Bivolt', 'Proteção contra surtos e queda de energia', 1450.00, 890.00, 'https://link.com/nobreak.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(21, 'Pasta Térmica Alta Performance', 'Condutividade térmica de 12.5 W/mK', 65.00, 12.00, 'https://link.com/thermal.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(22, 'Water Cooler 360mm RGB', 'Sistema de resfriamento líquido para CPUs potentes', 980.00, 520.00, 'https://link.com/wc360.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null),
(23, 'Gabinete Mid-Tower Glass', 'Lateral em vidro temperado e 4 fans inclusas', 540.00, 210.00, 'https://link.com/case.jpg', 1, 'ACTIVE', CURRENT_TIMESTAMP, 'admin', 'PRODUCT', null);

-- Inserindo os detalhes técnicos na tabela products
INSERT INTO products (id, sku, stock_quantity, type) VALUES
(4, 'CPU-AMD-R9-7950', 25, 'PHYSICAL'),
(5, 'GPU-NV-RTX4080S', 10, 'PHYSICAL'),
(6, 'MEM-DDR5-32GB', 100, 'PHYSICAL'),
(7, 'SSD-NVME-2TB', 80, 'PHYSICAL'),
(8, 'MON-4K-144-27', 15, 'PHYSICAL'),
(9, 'KB-MECH-RGB', 45, 'PHYSICAL'),
(10, 'SOFT-WIN11-PRO', NULL, 'DIGITAL'),
(11, 'SOFT-AV-TS-1Y', NULL, 'DIGITAL'),
(12, 'NET-WIFI6-AX30', 30, 'PHYSICAL'),
(13, 'AUD-71-SURROUND', 60, 'PHYSICAL'),
(14, 'IOT-BULB-RGB', 200, 'PHYSICAL'),
(15, 'CAM-4K-STREAM', 20, 'PHYSICAL'),
(16, 'LAP-WORK-I9-64', 5, 'PHYSICAL'),
(17, 'MSE-WIRE-25K', 75, 'PHYSICAL'),
(18, 'STR-HD-EXT-4TB', 40, 'PHYSICAL'),
(19, 'AUD-MIC-USB', 35, 'PHYSICAL'),
(20, 'PWR-UPS-1500VA', 12, 'PHYSICAL'),
(21, 'ACC-THERM-PAST', 150, 'PHYSICAL'),
(22, 'COOL-WC-360', 18, 'PHYSICAL'),
(23, 'CASE-MID-GLASS', 22, 'PHYSICAL');