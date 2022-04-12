package com.willhamhill.football.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Score {
    @Id
    private  String id;
    private String teamA;
    private String teamB;
    private int teamAGoals;
    private int teamBGoals;
}
