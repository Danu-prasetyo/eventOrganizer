package com.example.eventOrganizer.model;

import jakarta.persistence.*;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Relasi Many-to-One ke entitas table events
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id") // FK dari tabel participants
    private Event event;

    @Column(name = "participant_name")
    private String name;

    @Column(name = "email")
    private String email;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
