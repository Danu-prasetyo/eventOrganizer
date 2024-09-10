package com.example.eventOrganizer.services;
// STEP 4
import com.example.eventOrganizer.model.Event;
import com.example.eventOrganizer.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// @Service adalah anotasi Spring yang berfungsi untuk menyediakan logika bisnis seperti manipulasi data
@Service
public class EventServices {
    // @Autowired menginjeksi dependensi EventRepository ke dalam EventService.
    @Autowired
    private EventRepository eventRepository;

    public Page<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> searchEventByTitle(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }
}
