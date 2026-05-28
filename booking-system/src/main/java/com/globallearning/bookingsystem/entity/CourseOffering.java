package com.globallearning.bookingsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "offerings")
@Getter
@Setter
public class CourseOffering {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String courseTitle;  // e.g., "Python Coding"
    private String offeringName; // e.g., "Saturday Batch"
    private String teacherId;    // e.g., "teacher_101"

    @OneToMany(mappedBy = "courseOffering", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Session> sessions;
}