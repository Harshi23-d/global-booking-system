package com.globallearning.bookingsystem.service;

import com.globallearning.bookingsystem.entity.Booking;
import com.globallearning.bookingsystem.entity.CourseOffering;
import com.globallearning.bookingsystem.entity.Session;
import com.globallearning.bookingsystem.repository.BookingRepository;
import com.globallearning.bookingsystem.repository.CourseOfferingRepository;
import com.globallearning.bookingsystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private CourseOfferingRepository offeringRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 1. TEACHER: Create an offering with sessions converted to UTC
    @Transactional
    public CourseOffering createOffering(String courseTitle, String offeringName, String teacherId, List<SessionRequest> sessionRequests) {
        CourseOffering offering = new CourseOffering();
        offering.setCourseTitle(courseTitle);
        offering.setOfferingName(offeringName);
        offering.setTeacherId(teacherId);

        List<Session> sessions = new ArrayList<>();

        for (SessionRequest req : sessionRequests) {
            // Parse local time string into a LocalDateTime object
            LocalDateTime localDateTime = LocalDateTime.parse(req.getLocalStartTime(), formatter);
            
            // Link it with the teacher's specified timezone
            ZoneId teacherZone = ZoneId.of(req.getTimezone());
            ZonedDateTime zonedTeacherTime = ZonedDateTime.of(localDateTime, teacherZone);
            
            // Convert to a global UTC Instant to store in the database
            Session session = new Session();
            session.setCourseOffering(offering);
            session.setTeacherId(teacherId);
            session.setStartTime(zonedTeacherTime.toInstant());
            session.setEndTime(zonedTeacherTime.toInstant().plusSeconds(req.getDurationInMinutes() * 60L));

            sessions.add(session);
        }

        offering.setSessions(sessions);
        return offeringRepository.save(offering);
    }

    // 2. TEACHER: View all offerings managed by a teacher
    public List<CourseOffering> getTeacherOfferings(String teacherId) {
        return offeringRepository.findByTeacherId(teacherId);
    }

    // 3. PARENT: View all available offerings open for booking
    public List<CourseOffering> getAllOfferings() {
        return offeringRepository.findAll();
    }

    // 4. PARENT: View all offerings booked by a parent
    public List<Booking> getParentBookings(String parentId) {
        return bookingRepository.findByParentId(parentId);
    }

    // 5. PARENT: Book an entire offering with explicit overlap checking
    @Transactional
    public Booking bookOffering(String parentId, Long offeringId) {
        // Fetch the target offering the parent wants to book
        CourseOffering targetOffering = offeringRepository.findById(offeringId)
                .orElseThrow(() -> new RuntimeException("Course offering not found!"));

        // Fetch all current bookings for this parent
        List<Booking> existingBookings = bookingRepository.findByParentId(parentId);

        // Conflict check: compare every session in the target offering against all already booked sessions
        for (Session newSession : targetOffering.getSessions()) {
            for (Booking existingBooking : existingBookings) {
                for (Session bookedSession : existingBooking.getCourseOffering().getSessions()) {
                    
                    // Overlap Condition Formula: StartA < EndB AND EndA > StartB
                    if (newSession.getStartTime().isBefore(bookedSession.getEndTime()) && 
                        newSession.getEndTime().isAfter(bookedSession.getStartTime())) {
                        throw new RuntimeException("Booking failed! Time conflict detected with session in " 
                                + existingBooking.getCourseOffering().getCourseTitle());
                    }
                }
            }
        }

        // Save booking if no conflict is found
        Booking booking = new Booking();
        booking.setParentId(parentId);
        booking.setCourseOffering(targetOffering);
        booking.setBookedAt(java.time.Instant.now());

        return bookingRepository.save(booking);
    }
}