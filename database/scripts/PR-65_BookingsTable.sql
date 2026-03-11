drop table if exists bookings;
drop index if exists idx_booking_customer;
drop index if exists idx_booking_status;

CREATE TABLE bookings (
    booking_id BIGSERIAL PRIMARY KEY,

    customer_id BIGINT NOT NULL,

    pickup_address TEXT NOT NULL,
    pickup_latitude DECIMAL(10,7),
    pickup_longitude DECIMAL(10,7),

    drop_address TEXT NOT NULL,
    drop_latitude DECIMAL(10,7),
    drop_longitude DECIMAL(10,7),

    vehicle_type VARCHAR(50),

    scheduled_at TIMESTAMP,
    
    status VARCHAR(50) NOT NULL,
    
    estimated_distance_km DECIMAL(8,2),
    estimated_duration_minutes BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,

    CONSTRAINT fk_booking_customer
        FOREIGN KEY (customer_id) REFERENCES users(id)
);

CREATE INDEX idx_booking_customer
ON bookings(customer_id);

CREATE INDEX idx_booking_status
ON bookings(status);

alter table bookings add column coupon_cd VARCHAR(50);
alter table bookings add column city VARCHAR(50);
alter table bookings add column state VARCHAR(50);
alter table bookings add column country VARCHAR(50);