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


    fun watchCustomers() : ChangeStreamPublisher<Customer>{
        logger.debug { "watching customers" }
        return MongoDbManager.database.getCollection<Customer>().watch<Customer>().publisher
    }

    fun watchEmployee() : ChangeStreamPublisher<Employee>{
        logger.debug { "watching Employee" }
        return MongoDbManager.database.getCollection<Employee>().watch<Employee>().publisher
    }

    fun watchStringer() : ChangeStreamPublisher<Stringer>{
        logger.debug { "watching Stringer" }
        return MongoDbManager.database.getCollection<Stringer>().watch<Stringer>().publisher
    }

    fun watchCustomizer() : ChangeStreamPublisher<Customizer>{
        logger.debug { "watching Customizer" }
        return MongoDbManager.database.getCollection<Customizer>().watch<Customizer>().publisher
    }

    fun watchOrder() : ChangeStreamPublisher<Order>{
        logger.debug { "watching Order" }
        return MongoDbManager.database.getCollection<Order>().watch<Order>().publisher
    }

    fun watchProduct() : ChangeStreamPublisher<Product>{
        logger.debug { "watching Product" }
        return MongoDbManager.database.getCollection<Product>().watch<Product>().publisher
    }

    fun watchRacket() : ChangeStreamPublisher<Racket>{
        logger.debug { "watching Racket" }
        return MongoDbManager.database.getCollection<Racket>().watch<Racket>().publisher
    }

    fun watchTask() : ChangeStreamPublisher<Task>{
        logger.debug { "watching Task" }
        return MongoDbManager.database.getCollection<Task>().watch<Task>().publisher
    }
}