package com.example.tennislabspring.repositories.machines

import com.example.tennislabspring.model.machines.Customizer
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomizerRepository: CoroutineCrudRepository<Customizer, String> {
}