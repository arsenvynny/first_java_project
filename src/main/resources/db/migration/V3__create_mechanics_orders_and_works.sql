CREATE TABLE IF NOT EXISTS mechanics (
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS mechanic_specializations (
    mechanic_id    BIGINT NOT NULL REFERENCES mechanics (id) ON DELETE CASCADE,
    specialization VARCHAR(30) NOT NULL,
    PRIMARY KEY (mechanic_id, specialization)
);

CREATE TABLE IF NOT EXISTS orders (
    id          BIGSERIAL PRIMARY KEY,
    status      VARCHAR(20) NOT NULL,
    car_id      BIGINT NOT NULL REFERENCES cars (id),
    mechanic_id BIGINT REFERENCES mechanics (id),
    priority    VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS works (
    id              BIGSERIAL PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    completed       BOOLEAN NOT NULL DEFAULT FALSE,
    work_cost       NUMERIC(12, 2) NOT NULL,
    auto_parts_cost NUMERIC(12, 2) NOT NULL,
    specialization  VARCHAR(30) NOT NULL,
    order_id        BIGINT REFERENCES orders (id)
);

DROP INDEX IF EXISTS idx_orders_car_id;
CREATE INDEX idx_orders_car_id ON orders (car_id);

DROP INDEX IF EXISTS idx_works_order_id;
CREATE INDEX idx_works_order_id ON works (order_id);

DROP INDEX IF EXISTS idx_mechanic_specializations_mechanic_id;
CREATE INDEX idx_mechanic_specializations_mechanic_id ON mechanic_specializations (mechanic_id);

DROP INDEX IF EXISTS idx_orders_mechanic_id;
CREATE INDEX idx_orders_mechanic_id ON orders (mechanic_id);

DROP INDEX IF EXISTS idx_mechanic_specializations_specialization;
CREATE INDEX idx_mechanic_specializations_specialization ON mechanic_specializations (specialization);