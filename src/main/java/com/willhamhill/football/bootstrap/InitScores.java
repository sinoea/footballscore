package com.willhamhill.football.bootstrap;

import com.willhamhill.football.model.Score;
import com.willhamhill.football.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitScores  implements ApplicationListener<ApplicationReadyEvent> {

    private final ScoreRepository scoreRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Score score1 = Score.builder().teamA("Chelsea").teamB("Fulham").teamAGoals(0).teamBGoals(2).build();
        Score score2 = Score.builder().teamA("Manchester").teamB("Liverpool").teamAGoals(1).teamBGoals(3).build();
        Score score3 = Score.builder().teamA("Liverpool").teamB("Eastham").teamAGoals(5).teamBGoals(1).build();
        Score score4 = Score.builder().teamA("Birmingham").teamB("Chelsea").teamAGoals(6).teamBGoals(7).build();

        this.scoreRepository
                .deleteAll()
                .thenMany(
                        Flux.just(score1,score2,score3,score4)
                                .flatMap(scoreRepository::save)
                ).thenMany(scoreRepository.findAll())
                .subscribe(p -> {
                            log.info("Score: " + p.toString());
                    }
                );
    }
}
