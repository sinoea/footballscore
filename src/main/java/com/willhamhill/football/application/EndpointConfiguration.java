package com.willhamhill.football.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class EndpointConfiguration {

    /**
     * This RouterFunction bean can be used instead of the REST ScoreController.
     * I have included both to showcase both types of implementations.
     * @param handler
     * @return
     */
    @Bean
    RouterFunction<ServerResponse> routes(ScoreHandler handler) {
        return route(routeRequest(GET("/scores")), handler::all)
                .andRoute(routeRequest(GET("/scores/{id}")), handler::getById)
                .andRoute(routeRequest(DELETE("/scores/{id}")), handler::deleteById)
                .andRoute(routeRequest(POST("/scores")), handler::create)
                .andRoute(routeRequest(PUT("/scores/{id}")), handler::updateById);
    }

    private static RequestPredicate routeRequest(RequestPredicate target) {
        return new CaseInsensitiveRequestPredicate(target);
    }
}