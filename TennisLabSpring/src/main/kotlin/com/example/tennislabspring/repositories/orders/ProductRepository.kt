package com.example.tennislabspring.repositories.orders

import com.example.tennislabspring.model.Product
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: CoroutineCrudRepository<Product, String> {
}