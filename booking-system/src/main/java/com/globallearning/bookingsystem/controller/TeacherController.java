package com.globallearning.bookingsystem.controller;

import com.globallearning.bookingsystem.entity.CourseOffering;
import com.globallearning.bookingsystem.service.BookingService;
import com.globallearning.bookingsystem.service.SessionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private BookingService bookingService;

    // Endpoint for a teacher to create an offering with sessions
    @PostMapping("/{teacherId}/offerings")
    public ResponseEntity<CourseOffering> createOffering(
            @PathVariable String teacherId,
            @RequestParam String courseTitle,
            @RequestParam String offeringName,
            @RequestBody List<SessionRequest> sessions) {
        
        CourseOffering created = bookingService.createOffering(courseTitle, offeringName, teacherId, sessions);
        return ResponseEntity.ok(created);
    }

    // Endpoint for a teacher to view their upcoming schedule
    @GetMapping("/{teacherId}/offerings")
    public ResponseEntity<List<CourseOffering>> getTeacherOfferings(@PathVariable String teacherId) {
        return ResponseEntity.ok(bookingService.getTeacherOfferings(teacherId));
    }
}