package com.microservice.webflux.chapter4

class CustomerExistException(override val message: String): Exception(message)