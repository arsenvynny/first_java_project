CREATE TABLE IF NOT EXISTS cars (
    id       BIGSERIAL PRIMARY KEY,
    model    VARCHAR(50) NOT NULL,
    mark     VARCHAR(50) NOT NULL,
    year     INTEGER NOT NULL,
    owner_id BIGINT NOT NULL REFERENCES clients (id),
    vin_code VARCHAR(8) UNIQUE NOT NULL
);

DROP INDEX IF EXISTS idx_cars_owner_id;
CREATE INDEX idx_cars_owner_id ON cars (owner_id);
