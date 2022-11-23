package com.microservice.webflux.chapter4

import com.microservice.webflux.chapter4.Customer.Telephone
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap


@Component
class CustomerServiceImpl : CustomerService {
    companion object { // 테스트 데이터 초기화
        val initialCustomers = arrayOf(Customer(1, "Kotlin"),
            Customer(2, "Spring"),
            Customer(3, "MicroService", Telephone("+44", "7123456789"))
        )
    }

    private val customers = ConcurrentHashMap<Int, Customer>(initialCustomers.associateBy(Customer::id))

    override fun getCustomer(id: Int): Mono<Customer> = customers[id]?.toMono() ?: Mono.empty() // null 값 없도록 처리

    override fun searchCustomers(nameFilter: String): Flux<Customer> =
        customers.filter {
            it.value.name.contains(nameFilter, true)
        }.map(Map.Entry<Int, Customer>::value).toFlux()
    
    
    // CustomerExistException 추가
    override fun createCustomer(customerMono: Mono<Customer>) =
        customerMono.flatMap {
            if (customers[it.id] == null) {
                customers[it.id] = it
                it.toMono()
            } else {
                Mono.error(CustomerExistException("Customer ${it.id} already exist"))
            }
        }
}