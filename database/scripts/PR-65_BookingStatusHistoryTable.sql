drop table if exists bookings_history;
drop index if exists idx_status_history_booking;
drop index if exists idx_status_history_changed_at;

CREATE TABLE bookings_history (
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
    audited_at TIMESTAMP,
    audited_by BIGINT,

CONSTRAINT fk_booking_history_customer
FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_booking_history_id
        FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

alter table bookings_history add column coupon_cd VARCHAR(50);
alter table bookings_history add column city VARCHAR(50);
alter table bookings_history add column state VARCHAR(50);
alter table bookings_history add column country VARCHAR(50);

CREATE INDEX idx_status_history_booking
ON bookings_history(booking_id);

CREATE INDEX idx_status_history_changed_at
ON bookings_history(audited_at);