package com.porter_replica.auth_service.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.porter_replica.auth_service.auth.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	Optional<User> findByPhone(String phone);
	@Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier")
	Optional<User> findByEmailOrPhone(@Param("identifier") String identifier);
}
