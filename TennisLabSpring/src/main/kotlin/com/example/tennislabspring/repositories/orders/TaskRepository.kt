package com.example.tennislabspring.repositories.orders

import com.example.tennislabspring.model.orders.tasks.Task
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: CoroutineCrudRepository<Task, String> {
}