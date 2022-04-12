package com.willhamhill.test.services;

import com.willhamhill.football.model.Score;
import com.willhamhill.football.repository.ScoreRepository;
import com.willhamhill.football.services.ScoreService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.List;
import java.util.UUID;

@Slf4j
@ComponentScan(basePackages = "com.willhamhill.football")
@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootTest(classes = {ScoreService.class})
public class ScoreServiceTest {

    private final ScoreService scoreService;
    private final ScoreRepository scoreRepository;

    private Score score1;
    private Score score2;
    private Score score3;
    private Score score4;

    public ScoreServiceTest(@Autowired ScoreService scoreService, @Autowired ScoreRepository scoreRepository) {
        this.scoreService = scoreService;
        this.scoreRepository = scoreRepository;
    }

    @BeforeEach
    public void setup() {
        score1 = Score.builder().teamA("Fulham").teamB("Liverpool").teamAGoals(1).teamBGoals(9).build();
        score2 = Score.builder().teamA("Eastham").teamB("Liverpool").teamAGoals(10).teamBGoals(3).build();
        score3 = Score.builder().teamA("Liverpool").teamB("Chelsea").teamAGoals(3).teamBGoals(2).build();
        score4 = Score.builder().teamA("Eastham").teamB("Chelsea").teamAGoals(6).teamBGoals(0).build();

    }

    @Test
    public void getAll() {
        Flux<Score> saved = scoreRepository.saveAll(
                Flux.just(
                        score1,
                        score2,
                        score3,
                        score4
                )
        );

        List<Score> composite = scoreService.getScores().thenMany(saved).collectList().block();
        composite.forEach(s -> log.info(s.toString()));
        Assertions.assertThat(composite).contains(score1, score2, score3, score4);
    }

    @Test
    public void save() {
        Mono<Score> scoreMono = this.scoreService.create(score1);
        StepVerifier
                .create(scoreMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void delete() {
        Mono<Score> deleted = this.scoreService
                .create(score1)
                .flatMap(toDelete -> this.scoreService.deleteById(toDelete.getId()));

        StepVerifier
                .create(deleted)
                .expectNextMatches(score -> score.equals(score1))
                .verifyComplete();
    }

    @Test
    public void update() throws Exception {
        Mono<Score> saved = this.scoreService
                .create(score1);
        score1.setTeamA("Madrid");
        score1.setTeamB("Barcelona");
        saved.flatMap(score -> this.scoreService.update(score));

        StepVerifier
                .create(saved)
                .expectNextMatches(p -> p.getTeamA().equals("Madrid") && p.getTeamB().equals("Barcelona"))
                .verifyComplete();
    }

    @Test
    public void getById() {
        String id = UUID.randomUUID().toString();
        score1.setId(id);
        Mono<Score> deleted = this.scoreService
                .create(score1)
                .flatMap(saved -> this.scoreService.getScoreById(saved.getId()));

        StepVerifier
                .create(deleted)
                .expectNextMatches(profile -> StringUtils.hasText(profile.getId()) && id.equalsIgnoreCase(profile.getId()))
                .verifyComplete();
    }
}
