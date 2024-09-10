package com.example.eventOrganizer.repository;
// STEP 3
import com.example.eventOrganizer.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository adalah anotasi Spring yang berfungsi sebagai repositori yang menangani operasi database
@Repository
// EventRepository extends JpaRepository ini menyediakan metode CRUD standar untuk entitas events
// JpaRepository<Event, Long> berarti repositori ini bekerja dengan entitas tabel Event dan ID bertipe Long.
public interface EventRepository extends JpaRepository<Event, Long> {
    // Method untuk mencari event berdasarkan title event
    /* JPA memakai query method keywords untuk membuat query secara otomatis berdasarkan nama metode, sehingga contoh dibawah ini menghasilkan query berikut:
     * SELECT * FROM events WHERE LOWER(title) LIKE LOWER('%searchedValue%'); */
    List<Event> findByTitleContainingIgnoreCase(String title);

    // Method untuk pagination
    Page<Event> findAll(Pageable pageable);
}
