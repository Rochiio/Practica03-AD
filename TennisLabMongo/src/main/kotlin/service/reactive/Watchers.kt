package service.reactive

import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import com.mongodb.reactivestreams.client.MongoDatabase
import db.MongoDbManager
import model.Product
import model.Racket
import model.machines.Customizer
import model.machines.Stringer
import model.orders.Order
import model.orders.tasks.Task
import model.users.Customer
import model.users.Employee
import mu.KotlinLogging
import mu.withLoggingContext
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger{}

@Single
class Watchers {

    fun watchOrder() : ChangeStreamPublisher<Order>{
        logger.info { "watching Order" }
        return MongoDbManager.database.getCollection<Order>().watch<Order>().publisher
    }

}