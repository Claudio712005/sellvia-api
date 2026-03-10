CREATE TABLE products (
    id BIGINT NOT NULL,
    sku VARCHAR(100) NOT NULL,
    stock_quantity INTEGER,
    type VARCHAR(50) NOT NULL,

    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku),
    CONSTRAINT fk_products_saleable_item
        FOREIGN KEY (id)
        REFERENCES saleable_items (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_products_sku ON products(sku);