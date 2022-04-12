package com.willhamhill.football.model;

import org.springframework.context.ApplicationEvent;

public class ScoreUpdatedEvent extends ApplicationEvent {

    public ScoreUpdatedEvent(Score score) {
        super(score);
    }
}
