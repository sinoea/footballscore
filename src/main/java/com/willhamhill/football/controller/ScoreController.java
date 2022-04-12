package com.willhamhill.football.controller;

import com.willhamhill.football.model.Score;
import com.willhamhill.football.services.ScoreService;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/scores", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/{scoreId}")
    public Publisher<Score> getScoreById(@PathVariable @NotNull String scoreId) {
        return scoreService.getScoreById(scoreId);
    }

    @GetMapping
    public Publisher<Score> getScores() {
        return scoreService.getScores();
    }

    @PostMapping
    Publisher<ResponseEntity<Score>> create(@RequestBody @Valid @NotNull Score score) {
        return this.scoreService
                .create(score)
                .map(p -> ResponseEntity.created(URI.create("/scores/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Score> deleteById(@PathVariable @NotNull String id) {
        return this.scoreService.deleteById(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Score>> updateById(@PathVariable String id, @RequestBody @Valid @NotNull Score score) {
        return Mono
                .just(score)
                .flatMap(p -> this.scoreService.update(score))
                .map(p -> ResponseEntity
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .build());
    }
}