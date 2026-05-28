package com.globallearning.bookingsystem.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionRequest {
    private String localStartTime; // Format: "YYYY-MM-DD HH:MM:SS" (e.g., "2026-06-06 18:00:00")
    private String timezone;       // e.g., "Asia/Kolkata", "America/New_York"
    private int durationInMinutes; // e.g., 60
}