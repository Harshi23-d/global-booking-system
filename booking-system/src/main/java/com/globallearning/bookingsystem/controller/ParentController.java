package com.globallearning.bookingsystem.controller;

import com.globallearning.bookingsystem.entity.Booking;
import com.globallearning.bookingsystem.entity.CourseOffering;
import com.globallearning.bookingsystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parents")
public class ParentController {

    @Autowired
    private BookingService bookingService;

    // Endpoint for parents to see all available classes globally
    @GetMapping("/offerings")
    public ResponseEntity<List<CourseOffering>> getAllOfferings() {
        return ResponseEntity.ok(bookingService.getAllOfferings());
    }

    // Endpoint for a parent to book an entire course offering
    @PostMapping("/{parentId}/bookings/{offeringId}")
    public ResponseEntity<?> bookOffering(
            @PathVariable String parentId,
            @PathVariable Long offeringId) {
        try {
            Booking booking = bookingService.bookOffering(parentId, offeringId);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            // Thoughtful error handling: returns clean error messages for overlaps or missing data
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint for a parent to view their dashboard of booked courses
    @GetMapping("/{parentId}/bookings")
    public ResponseEntity<List<Booking>> getParentBookings(@PathVariable String parentId) {
        return ResponseEntity.ok(bookingService.getParentBookings(parentId));
    }
}