CREATE TABLE cars (
    id BIGSERIAL PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    length_cm INTEGER NOT NULL CHECK (length_cm > 0 AND length_cm <= 1000),
    weight_kg INTEGER NOT NULL CHECK (weight_kg > 0 AND weight_kg <= 10000),
    max_velocity_kmh INTEGER NOT NULL CHECK (max_velocity_kmh > 0 AND max_velocity_kmh <= 500),
    color VARCHAR(50) NOT NULL CHECK (color ~ '^[a-zA-Z]+$'),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_cars_color ON cars(color);
CREATE INDEX idx_cars_model ON cars(model);