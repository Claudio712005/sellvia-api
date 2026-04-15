create sequence IF NOT EXISTS companies_id_seq
    START with 1
    INCREMENT BY 1;

create TABLE companies (
    id BIGINT NOT NULL DEFAULT nextval('companies_id_seq'),
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    business_name VARCHAR(255),
    website_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    company_url_logo VARCHAR(255),

    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    CONSTRAINT pk_companies PRIMARY KEY (id)
);

create index idx_companies_cnpj on companies(cnpj);