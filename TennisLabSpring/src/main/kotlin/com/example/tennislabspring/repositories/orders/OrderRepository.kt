package com.example.tennislabspring.repositories.orders

import com.example.tennislabspring.model.orders.Order
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: CoroutineCrudRepository<Order, String> {
}