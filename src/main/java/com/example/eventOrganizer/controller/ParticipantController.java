package com.example.eventOrganizer.controller;

import com.example.eventOrganizer.model.Event;
import com.example.eventOrganizer.model.Participant;
import com.example.eventOrganizer.services.EventServices;
import com.example.eventOrganizer.services.ParticipantServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/participants")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ParticipantController {
    @Autowired
    private ParticipantServices participantServices;
    @Autowired
    private EventServices eventServices;

    @GetMapping
    public ResponseEntity<Page<Participant>> getAllParticipants(
            @RequestParam(defaultValue = "0") int page,      // default page = 0
            @RequestParam(defaultValue = "10") int size      // default size = 10
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page<Participant> participants = participantServices.getAllParticipants(pageable);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        Optional<Participant> product = participantServices.getParticipantById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant product) {
        return participantServices.saveParticipant(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParticipant(@PathVariable Long id, @RequestBody Participant participantDetails) {
        Optional<Participant> participant = participantServices.getParticipantById(id);

        if (participant.isPresent()) {
            Participant existingParticipant = participant.get();

            existingParticipant.setName(participantDetails.getName());
            existingParticipant.setEmail(participantDetails.getEmail());

            if (participantDetails.getEmail() != null) {
                Long eventId = participantDetails.getEvent().getId();
                Optional<Event> event = eventServices.getEventById(eventId);

                if (event.isPresent()) {
                    existingParticipant.setEvent(event.get());

                    Participant updatedParticipant = participantServices.saveParticipant(existingParticipant);

                    return ResponseEntity.ok(updatedParticipant);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "404");
                    response.put("message", "Event not found");
                    return ResponseEntity.status(404).body(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "400");
                response.put("message", "Event is required");
                return ResponseEntity.status(400).body(response);
            }

        } else {
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "Participant not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteParticipant(@PathVariable Long id) {
        Optional<Participant> participant = participantServices.getParticipantById(id);

        if (participant.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "Participant not found");
            return ResponseEntity.status(404).body(response);
        }

        participantServices.deleteParticipant(id);

        Map<String, String> response = new HashMap<>();
        response.put("status", "200");
        response.put("message", "Delete successful");
        return ResponseEntity.ok(response);
    }
}
