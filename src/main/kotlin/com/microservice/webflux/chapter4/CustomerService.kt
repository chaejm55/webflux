package com.microservice.webflux.chapter4

interface CustomerService {
    fun getCustomer(id: Int): Customer?
    fun searchCustomers(nameFilter: String): List<Customer>
}