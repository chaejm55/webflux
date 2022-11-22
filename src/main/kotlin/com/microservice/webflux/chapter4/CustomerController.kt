package com.microservice.webflux.chapter4

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class CustomerController {
    @Autowired
    private lateinit var customerService: CustomerService

    @GetMapping("/customer/{id}")
    fun getCustomer(@PathVariable id: Int): ResponseEntity<Mono<Customer>> {
        val customer = customerService.getCustomer(id)
        return ResponseEntity(customer, HttpStatus.OK)
    }

    @GetMapping("/customers")
    fun getCustomers(@RequestParam(required = false, defaultValue = "")nameFilter: String) =
        customerService.searchCustomers(nameFilter)

    @PostMapping("/customer")
    fun createCustomer(@RequestBody customerMono: Mono<Customer>) =
        ResponseEntity(customerService.createCustomer(customerMono), HttpStatus.CREATED)
}