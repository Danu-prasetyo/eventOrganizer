package com.example.eventOrganizer.services;

import com.example.eventOrganizer.model.Participant;
import com.example.eventOrganizer.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParticipantServices {
    @Autowired
    private ParticipantRepository participantRepository;
    
    public Page<Participant> getAllParticipants(Pageable pageable) {
        return participantRepository.findAll(pageable);
    }
    
    public Optional<Participant> getParticipantById(Long id) {
        return participantRepository.findById(id);
    }
    
    public Participant saveParticipant(Participant product) {
        return participantRepository.save(product);
    }
    
    public void deleteParticipant(Long id) {
        participantRepository.deleteById(id);
    }
}
