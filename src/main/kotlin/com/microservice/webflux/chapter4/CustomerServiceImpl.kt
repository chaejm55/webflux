package com.microservice.webflux.chapter4

import com.microservice.webflux.chapter4.Customer.Telephone
import org.springframework.stereotype.Component
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

    override fun getCustomer(id: Int): Customer? = customers[id]

    override fun searchCustomers(nameFilter: String): List<Customer> =
        customers.filter {
            it.value.name.contains(nameFilter, true)
        }.map(Map.Entry<Int, Customer>::value).toList()
}