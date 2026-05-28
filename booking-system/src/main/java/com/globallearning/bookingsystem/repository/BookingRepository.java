package com.globallearning.bookingsystem.repository;

import com.globallearning.bookingsystem.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // This helper method finds all courses a specific parent has already booked
    List<Booking> findByParentId(String parentId);
}