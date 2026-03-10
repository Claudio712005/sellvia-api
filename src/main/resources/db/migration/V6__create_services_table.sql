CREATE TABLE services (
    id BIGINT NOT NULL,
    estimated_duration_in_minutes INTEGER NOT NULL,

    CONSTRAINT pk_services PRIMARY KEY (id),
    CONSTRAINT fk_services_saleable_item
        FOREIGN KEY (id)
        REFERENCES saleable_items (id)
        ON DELETE CASCADE
);