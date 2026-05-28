package com.globallearning.bookingsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "sessions")
@Getter
@Setter
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "offering_id", nullable = false)
    @JsonIgnore // Prevents infinite loops when rendering data as JSON
    private CourseOffering courseOffering;

    private String teacherId;
    
    // Instant automatically maps to a timestamp with timezone, storing everything in UTC
    private Instant startTime; 
    private Instant endTime;
}