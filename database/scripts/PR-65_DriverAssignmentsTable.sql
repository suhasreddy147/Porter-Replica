drop table if exists driver_assignments;
drop index if exists idx_driver_assignment_driver;
drop index if exists idx_driver_assignment_booking;
CREATE TABLE driver_assignments (

    driver_assignment_id BIGSERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL,
    driver_id BIGINT NOT NULL,

    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    status VARCHAR(50),
    reason VARCHAR(255),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,

    CONSTRAINT fk_assignment_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),

    CONSTRAINT fk_assignment_driver
        FOREIGN KEY (driver_id) REFERENCES users(id)
);

CREATE INDEX idx_driver_assignment_driver
ON driver_assignments(driver_id);
CREATE INDEX idx_driver_assignment_booking
ON driver_assignments(booking_id);