package com.porter_replica.booking_service.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.porter_replica.booking_service.entity.DriverAssignments;

@Repository
public interface DriverAssignmentsRepository extends JpaRepository<DriverAssignments, Long>{

	List<DriverAssignments> findByBookingId(Long bookingId);

}
