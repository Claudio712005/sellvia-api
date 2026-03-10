CREATE SEQUENCE IF NOT EXISTS saleable_items_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE saleable_items (
    id BIGINT NOT NULL DEFAULT nextval('saleable_items_id_seq'),
    item_type VARCHAR(31) NOT NULL,
    company_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL,
    production_cost NUMERIC(19, 2) NOT NULL,
    image_url VARCHAR(500),
    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT pk_saleable_items PRIMARY KEY (id),
    CONSTRAINT fk_items_company FOREIGN KEY (company_id) REFERENCES companies (id),
    CONSTRAINT fk_items_category FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE INDEX idx_saleable_items_company_id ON saleable_items(company_id);
CREATE INDEX idx_saleable_items_category_id ON saleable_items(category_id);
CREATE INDEX idx_saleable_items_status ON saleable_items(status);