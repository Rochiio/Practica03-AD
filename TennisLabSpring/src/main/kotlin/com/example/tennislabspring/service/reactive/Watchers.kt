package com.example.tennislabspring.service.reactive

import com.example.tennislabspring.model.orders.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.ChangeStreamEvent
import org.springframework.data.mongodb.core.ChangeStreamOptions
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger{}

@Service
class Watchers
    @Autowired constructor(
    private val reactiveMongoTemplate: ReactiveMongoTemplate
){

    fun watchOrder() : Flow<ChangeStreamEvent<Order>> {
        logger.info { "watching Order" }
        return reactiveMongoTemplate.changeStream(
            "order",
            ChangeStreamOptions.empty(),
            Order::class.java
        ).asFlow()
    }

}