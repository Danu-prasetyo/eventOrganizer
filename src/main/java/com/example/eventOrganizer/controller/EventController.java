package com.example.eventOrganizer.controller;

import com.example.eventOrganizer.model.Event;
import com.example.eventOrganizer.services.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/events")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EventController {
    @Autowired
    private EventServices eventServices;

    @GetMapping
    public ResponseEntity<Page<Event>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,      // default page = 0
            @RequestParam(defaultValue = "10") int size      // default size = 10
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Event> products = eventServices.getAllEvents(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> product = eventServices.getEventById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Event createEvent(@RequestBody Event product) {
        return eventServices.saveEvent(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        Optional<Event> product = eventServices.getEventById(id);

        if (product.isPresent()) {
            Event existingEvent = product.get();

            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setStartOn(eventDetails.getStartOn());
            existingEvent.setCompleteOn(eventDetails.getCompleteOn());
            existingEvent.setParticipant(eventDetails.getParticipant());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setVersion(eventDetails.getVersion());

            Event updatedEvent = eventServices.saveEvent(existingEvent);
            return ResponseEntity.ok(updatedEvent);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "Event not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEvent(@PathVariable Long id) {
        Optional<Event> product = eventServices.getEventById(id);

        if (product.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "Event not found");
            return ResponseEntity.status(404).body(response);
        }

        eventServices.deleteEvent(id);

        Map<String, String> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", "Delete successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvent(@RequestParam("event") String title) {
        List<Event> events = eventServices.searchEventByTitle(title);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }
}
