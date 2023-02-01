package com.example.tennislabspring.repositories.users

import com.example.tennislabspring.model.users.Customer
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository: CoroutineCrudRepository<Customer, String> {
    suspend fun findCustomerByEmail(email: String): Customer?
    suspend fun findCustomerByUuid(uuid: UUID): Customer?
}