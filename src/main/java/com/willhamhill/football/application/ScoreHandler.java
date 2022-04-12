package com.willhamhill.football.application;

import com.willhamhill.football.model.Score;
import com.willhamhill.football.services.ScoreService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.net.URI;

@Component
class ScoreHandler {

    private final ScoreService scoreService;

    ScoreHandler(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    Mono<ServerResponse> getById(ServerRequest r) {
        return defaultReadResponse(this.scoreService.getScoreById(id(r)));
    }

    Mono<ServerResponse> all(ServerRequest r) {
        return defaultReadResponse(this.scoreService.getScores());
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
        return defaultReadResponse(this.scoreService.deleteById(id(r)));
    }

    Mono<ServerResponse> updateById(ServerRequest r) {
        Flux<Score> id = r.bodyToFlux(Score.class)
                .flatMap(Score -> this.scoreService.update(Score));
        return defaultReadResponse(id);
    }

    Mono<ServerResponse> create(ServerRequest request) {
        Flux<Score> flux = request
                .bodyToFlux(Score.class)
                .flatMap(Score -> this.scoreService.create(Score));
        return defaultWriteResponse(flux);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Score> Score) {
        return Mono
                .from(Score)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/scores/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Score> Scores) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Scores, Score.class);
    }

    private static String id(ServerRequest r) {
        return r.pathVariable("id");
    }
}