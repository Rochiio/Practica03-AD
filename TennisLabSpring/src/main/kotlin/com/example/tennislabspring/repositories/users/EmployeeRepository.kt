package com.example.tennislabspring.repositories.users

import com.example.tennislabspring.model.users.Employee
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EmployeeRepository: CoroutineCrudRepository<Employee, String> {
    suspend fun findEmployeeByEmail(email:String):Employee?
    suspend fun findEmployeeByUuid(uuid: UUID): Employee?
}