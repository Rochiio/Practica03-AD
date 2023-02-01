package com.example.tennislabspring.repositories.machines

import com.example.tennislabspring.model.machines.Stringer
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface StringerRepository: CoroutineCrudRepository<Stringer, String> {
}