package com.willhamhill.football.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.willhamhill.football.application.ScoreUpdatedEventPublisher;
import com.willhamhill.football.model.ScoreUpdatedEvent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class LiveScoresController {

    private final Flux<ScoreUpdatedEvent> scoreUpdatedEvents;
    private final ObjectMapper objectMapper;

    public LiveScoresController(ScoreUpdatedEventPublisher eventPublisher, ObjectMapper objectMapper) {
        this.scoreUpdatedEvents = Flux.create(eventPublisher).share();
        this.objectMapper = objectMapper;
    }

    @GetMapping(path = "/live/scores", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public Flux<String> liveScores() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return this.scoreUpdatedEvents.map(scoreUpdatedEvent -> {
            try {
                return objectMapper.writeValueAsString(scoreUpdatedEvent.getSource());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}