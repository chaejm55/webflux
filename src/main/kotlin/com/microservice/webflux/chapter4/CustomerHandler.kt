package com.microservice.webflux.chapter4

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono
import java.net.URI

@Component
class CustomerHandler(val customerService: CustomerService) { // get 요청 응답 정의
    // 1. nullable 하지 않으므로 클래스 리플렉션 제거
    // 2. 핸들러에서 pathVariable 처리
    // 3. 무조건 ok 리턴이 아닌 오류 발생 시 404 오류 코드 리턴
    fun get(serverRequest: ServerRequest) =
        customerService.getCustomer(serverRequest.pathVariable("id").toInt())
            .flatMap { ok().body(fromValue(it)) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    // queryParam으로 옵셔널 값 사용
    fun search(serverRequest: ServerRequest) =
        ok().body(customerService.searchCustomers(serverRequest.queryParam("nameFilter").orElse("")),
            Customer::class.java)
    
    // 1. bodyToMono()로 본문을 Mono<Customer>로 변환
    // 2. 리소스 위치를 헤더로 반환하도록 변경
    fun create(serverRequest: ServerRequest) =
        customerService.createCustomer(serverRequest.bodyToMono()).flatMap {
            created(URI.create("/functional/customer/${it.id}")).build()
        }
}