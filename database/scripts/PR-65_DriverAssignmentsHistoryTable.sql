drop table if exists driver_assignments_history;
drop index if exists idx_driver_assignment_history_driver;
drop index if exists idx_driver_assignment_history_booking;
CREATE TABLE driver_assignments_history (

    driver_assignment_id BIGSERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL,

    driver_id BIGINT NOT NULL,
    
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    status VARCHAR(50) NOT NULL,

    reason VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_at TIMESTAMP,
    updated_by BIGINT,
    audited_at TIMESTAMP,
    audited_by BIGINT,

    CONSTRAINT fk_assignment_history_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(booking_id),

    CONSTRAINT fk_assignment_history_driver
        FOREIGN KEY (driver_id) REFERENCES users(id),
        
    CONSTRAINT fk_assignment_history_id
        FOREIGN KEY (driver_assignment_id) REFERENCES driver_assignments(driver_assignment_id)
);

CREATE INDEX idx_driver_assignment_history_driver
ON driver_assignments_history(driver_id);
CREATE INDEX idx_driver_assignment_history_booking
ON driver_assignments_history(booking_id);