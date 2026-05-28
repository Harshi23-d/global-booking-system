package com.globallearning.bookingsystem.repository;

import com.globallearning.bookingsystem.entity.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Long> {
    // This helper method finds all sections created by a specific teacher
    List<CourseOffering> findByTeacherId(String teacherId);
}