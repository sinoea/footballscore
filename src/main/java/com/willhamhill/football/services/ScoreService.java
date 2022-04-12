package com.willhamhill.football.services;

import com.willhamhill.football.model.Score;
import com.willhamhill.football.model.ScoreUpdatedEvent;
import com.willhamhill.football.repository.ScoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ScoreService {

    private final ApplicationEventPublisher publisher;
    private final ScoreRepository scoreRepository;

    public ScoreService(@Autowired ApplicationEventPublisher publisher, @Autowired ScoreRepository scoreRepository) {
        this.publisher = publisher;
        this.scoreRepository = scoreRepository;
    }

    public Mono<Score> getScoreById(String id) {
        return scoreRepository.findById(id);
    }

    public Flux<Score> getScores() {
        return scoreRepository.findAll();
    }

    public Mono<Score> update(Score score) {
        return this.scoreRepository
                .findById(score.getId())
                .map(p -> new Score(score.getId(), score.getTeamA(), score.getTeamB(), score.getTeamAGoals(), score.getTeamBGoals()))
                .flatMap(this.scoreRepository::save).doOnSuccess(s -> this.publisher.publishEvent(new ScoreUpdatedEvent(s)));
    }

    public Mono<Score> deleteById(String id) {
        return this.scoreRepository
                .findById(id)
                .flatMap(score -> this.scoreRepository.deleteById(score.getId()).thenReturn(score));
    }

    public Mono<Score> create(Score score) {
        return this.scoreRepository
                .save(score);
    }
}
