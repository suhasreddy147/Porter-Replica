drop table if exists booking_pricing;
drop index if exists idx_pricing_booking;
CREATE TABLE booking_pricing (

    pricing_id BIGSERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL,

    base_fare DECIMAL(10,2),

    distance_fare DECIMAL(10,2),

    time_fare DECIMAL(10,2),

    surge_amount DECIMAL(10,2),
    
    platform_fee DECIMAL(10,2),
    
    cgst_rate DECIMAL(5,2),

    cgst_amount DECIMAL(10,2),

    sgst_rate DECIMAL(5,2),

    sgst_amount DECIMAL(10,2),
    
    discount DECIMAL(10,2),

    final_price DECIMAL(10,2),

    currency VARCHAR(10) default 'INR',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,

    updated_at TIMESTAMP,
    updated_by BIGINT,

    CONSTRAINT fk_pricing_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

CREATE UNIQUE INDEX idx_pricing_booking
ON booking_pricing(booking_id);