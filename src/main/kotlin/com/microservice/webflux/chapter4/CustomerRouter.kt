package com.microservice.webflux.chapter4

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class CustomerRouter(private val customerHandler: CustomerHandler) {
    @Bean
    fun customerRoutes() = router {
        "/functional".nest {
            "/customer".nest {
                GET("/{id}", customerHandler::get) // @PathVariable??
                POST("", customerHandler::create)
            }
            "/customers".nest {
                GET("", customerHandler::search)
            }
        }
    }
}