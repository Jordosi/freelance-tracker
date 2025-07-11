ALTER TABLE clients ADD COLUMN is_global BOOLEAN DEFAULT false;
ALTER TABLE clients ADD COLUMN created_by BIGINT;
ALTER TABLE clients ADD COLUMN is_archived BOOLEAN DEFAULT false;

ALTER TABLE clients
    ADD CONSTRAINT fk_clients_created_by
        FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL;