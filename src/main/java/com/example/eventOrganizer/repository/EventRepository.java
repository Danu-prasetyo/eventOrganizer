package com.example.eventOrganizer.repository;

import com.example.eventOrganizer.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByTitleContainingIgnoreCase(String title);

    // Method untuk pagination
    Page<Event> findAll(Pageable pageable);
}
