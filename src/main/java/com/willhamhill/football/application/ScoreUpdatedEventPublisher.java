package com.willhamhill.football.application;

import com.willhamhill.football.model.ScoreUpdatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public
class ScoreUpdatedEventPublisher implements ApplicationListener<ScoreUpdatedEvent>, Consumer<FluxSink<ScoreUpdatedEvent>> {

    private final Executor executor;
    private final BlockingQueue<ScoreUpdatedEvent> queue = new LinkedBlockingQueue<>();

    ScoreUpdatedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(ScoreUpdatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<ScoreUpdatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    ScoreUpdatedEvent event = queue.take();
                    sink.next(event);
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}