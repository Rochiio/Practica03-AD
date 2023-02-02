package com.example.tennislabspring.repositories.rackets

import com.example.tennislabspring.model.Racket
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RacketRepository: CoroutineCrudRepository<Racket, String> {
}