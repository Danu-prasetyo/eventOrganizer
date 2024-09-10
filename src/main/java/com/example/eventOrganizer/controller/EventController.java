package com.example.eventOrganizer.controller;
// STEP 5
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

// @RestController : controller Spring yang bertugas sebagai RESTfull web service yang menangani permintaan HTTP
@RestController
// @RequestMapping : anotasi Spring untuk menetapkan base URL untuk semua endpoint di controller ini
@RequestMapping("/api/events")
// @EnableSpringDataWebSupport : mengaktifkan serialisasi secara global agar data JSON yang dihasilkan dari perubahan JSON ke objek Page(pagination) jadi lebih stabil
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class EventController {
    // @Autowired menginjeksi dependensi EventService ke dalam EventController
    @Autowired
    private EventServices eventServices;

    // @GetMapping menangani permintaan GET ke endpoint "/api/events"
    // Method untuk mengembalikan data semua events
    @GetMapping
    public ResponseEntity<Page<Event>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,      // default page = 0
            @RequestParam(defaultValue = "10") int size      // default size = 10
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title"));
        Page<Event> events = eventServices.getAllEvents(pageable);
        return ResponseEntity.ok(events);
    }

    // @GetMapping menangani permintaan GET ke endpoint "/api/events/{id}"
    // Method untuk mengembalikan event berdasarkan ID, atau 404 jika data tidak ditemukan
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        /* Optional : wadah/pembungkus yang menampung data dari entitas Event(model)
         * dan menangani error NullPointerException jika data tidak ada/ditemukan tanpa harus menggunakan nilai null */
        Optional<Event> events = eventServices.getEventById(id);
        return events.map(ResponseEntity::ok)// <- method yang mengirim status 200 OK yang artinya data tersebut berhasil ditemukan lalu akan memanggil Optional<Event> yang berisi data yang ditemukan
                .orElseGet(() -> ResponseEntity.notFound().build()); // <- metod yang mengirim status 404 not found yang artinya data tidak ditemukan
    }

    // @PostMapping menangani permintaan POST ke endpoint "/api/events"
    // Method untuk menambahkan event baru ke database
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventServices.saveEvent(event);
    }

    // @PutMapping menangani permintaan PUT ke endpoint "/api/events/{id}"
    // ResponseEntity<?> : untuk mengembalikan respons yang fleksibel, baik itu data event atau map berisi pesan error.
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        // Cek apakah event dengan ID yang dikirim ada di database
        Optional<Event> event = eventServices.getEventById(id);

        if (event.isPresent()) {
            Event existingEvent = event.get();

            // Update field event yang ada dengan data baru
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setStartOn(eventDetails.getStartOn());
            existingEvent.setCompleteOn(eventDetails.getCompleteOn());
            existingEvent.setParticipant(eventDetails.getParticipant());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setVersion(eventDetails.getVersion());

            // Simpan perubahan event
            Event updatedEvent = eventServices.saveEvent(existingEvent);

            // Kembalikan respons dengan event yang diupdate
            return ResponseEntity.ok(updatedEvent);
        } else {
            // Jika event tidak ditemukan, kembalikan respons 404 untuk event
            Map<String, String> response = new HashMap<>();
            response.put("status", "404");
            response.put("message", "Event not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    // @DeleteMapping menangani permintaan DELETE ke endpoint "/api/events/{id}"
    // Method untuk menghapus ebent berdasarkan ID dari database
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEvent(@PathVariable Long id) {
        Optional<Event> event = eventServices.getEventById(id);

        if (event.isEmpty()) {
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

    // Endpoint untuk mencari event berdasarkan nama event
    // @RequestParam("query_parameter_name"): untuk menangkap parameter query event dari URL, misalnya /api/events/search?event=phone.
    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvent(@RequestParam("event") String title) {
        List<Event> events = eventServices.searchEventByTitle(title);
        if (events.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(events);
    }
}
