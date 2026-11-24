CREATE TABLE events (
    id          BIGSERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    time        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type        VARCHAR(30) NOT NULL,
    order_id    BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE
);

CREATE INDEX idx_events_order_id ON events (order_id);
