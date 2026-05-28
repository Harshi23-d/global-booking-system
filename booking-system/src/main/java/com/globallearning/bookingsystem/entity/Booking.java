package com.globallearning.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(
    name = "bookings",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"parentId", "offering_id"})}
)
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String parentId; // ID of the student's parent

    @ManyToOne
    @JoinColumn(name = "offering_id", nullable = false)
    private CourseOffering courseOffering;

    private Instant bookedAt;
}