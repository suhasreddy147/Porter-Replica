package com.porter_replica.booking_service.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porter_replica.booking_service.entity.Bookings;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long>{

	List<Bookings> findByCustomerId(Long customerId);

}
