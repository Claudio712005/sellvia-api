CREATE SEQUENCE IF NOT EXISTS categories_id_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE categories (
    id BIGINT NOT NULL DEFAULT nextval('categories_id_seq'),
    company_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT fk_categories_company
        FOREIGN KEY (company_id)
        REFERENCES companies (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_categories_company_id ON categories(company_id);