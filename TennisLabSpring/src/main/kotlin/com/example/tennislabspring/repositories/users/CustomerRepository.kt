package com.example.tennislabspring.repositories.users

import com.example.tennislabspring.model.users.Customer
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository: CoroutineCrudRepository<Customer, String> {
    fun findByEmail(email: String): Customer?
    fun findByUuid(uuid: UUID): Customer?
}