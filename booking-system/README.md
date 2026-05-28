# Global Class Offering Booking System — Backend Service

A production-ready Spring Boot backend service for a global live-learning platform. This service allows teachers across different timezones to schedule multi-session course offerings, while enabling parents and students worldwide to view schedules and book batches safely without schedule overlaps.

---

## 🛠️ Technical Stack
* **Framework:** Java 17 + Spring Boot (v3.2.5)
* **Database:** H2 In-Memory Database (PostgreSQL-compatible)
* **ORM Layer:** Spring Data JPA with Hibernate

---

## 💡 Core Engineering Approach

### 1. Proper Timezone Handling (UTC Standard)
* **The Strategy:** To ensure schedule consistency across different continents, the backend converts all schedule metrics to **UTC** before storing them in the database.
* **How it works:** We use Java's high-precision `Instant` data type in the database layer. When a teacher creates a session using their local time format (e.g., `"2026-06-06 18:00:00"`) and specifies their home timezone (e.g., `"Asia/Kolkata"`), the `BookingService` maps it using `ZonedDateTime` and shifts it down to a standardized UTC timeline.

### 2. Conflict Detection Logic (Time Lock Overlap Rule)
* When a parent attempts to book a multi-session course offering, the system protects them from overbooking by comparing the entire new batch timeline against all of their existing bookings.
* It uses the standard time-interval overlap condition:
  $$\text{NewSession.StartTime} < \text{BookedSession.EndTime} \quad \text{AND} \quad \text{NewSession.EndTime} > \text{BookedSession.StartTime}$$
* If even one session within the new multi-week offering violates this constraint, the transaction fails immediately, throwing a descriptive conflict error message.

### 3. Concurrency & Data Consistency
* **Thread Safety:** The core logic is isolated inside Spring's `@Transactional` boundary manager, ensuring database operations execute atomically.
* **Double-Booking Prevention:** To safeguard against rapid double-clicks on the front end or parallel API booking requests, a strict **Unique Compound Constraint** is placed on `(parent_id, offering_id)` inside the `bookings` database table.

---

## 📡 Exposed REST API Endpoints

### Teacher APIs
* `POST /api/teachers/{teacherId}/offerings?courseTitle=...&offeringName=...`
  * **Description:** Creates a course offering with custom session frequencies, automatically shifting the teacher's local timezone input to standard UTC.
  * **Body Format (JSON List):**
    ```json
    [
      {
        "localStartTime": "2026-06-06 18:00:00",
        "timezone": "Asia/Kolkata",
        "durationInMinutes": 60
      }
    ]
    ```
* `GET /api/teachers/{teacherId}/offerings`
  * **Description:** Retrieves the upcoming teaching schedule for a specific teacher.

### Parent APIs
* `GET /api/parents/offerings`
  * **Description:** Displays all available learning batches globally.
* `POST /api/parents/{parentId}/bookings/{offeringId}`
  * **Description:** Reserves an entire multi-session course block while validating global timezone overlaps. Returns a clean error if an overlapping class exists.
* `GET /api/parents/{parentId}/bookings`
  * **Description:** Fetches the student's personal active course dashboard.

---

## 🖥️ How to Run & Verify

1. Open a terminal in the root project folder and compile the code:
   ```bash
   .\mvnw.cmd clean compile