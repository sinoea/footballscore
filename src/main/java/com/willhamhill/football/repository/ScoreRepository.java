package com.willhamhill.football.repository;

import com.willhamhill.football.model.Score;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ScoreRepository extends ReactiveMongoRepository<Score, String> {
}