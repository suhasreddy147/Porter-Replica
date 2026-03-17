package com.porter_replica.booking_service.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porter_replica.booking_service.entity.BookingPricing;

@Repository
public interface BookingPricingRepository extends JpaRepository<BookingPricing, Long>{

	List<BookingPricing> findByBookingId(Long bookingId);

}
