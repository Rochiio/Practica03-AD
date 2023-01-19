package repositories.orders

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import model.orders.Order
import model.users.Customer
import mu.KotlinLogging
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.setTo
import java.util.*

class OrderRepositoryImpl : OrderRepository {
    private var logger = KotlinLogging.logger{}
    private  var dbMongo = MongoDbManager.database
    override suspend fun findById(id: UUID): Order? {
        logger.debug { "Buscando tarea con id: $id" }
        return dbMongo.getCollection<Order>().findOneById(id)
    }

    override suspend fun save(item: Order): Order {
        logger.debug { "Guardando tarea: $item" }
        return dbMongo.getCollection<Order>()
            .save(item).let { item }
    }

    override suspend fun update(item: Order): Order {
        logger.debug { "Actualizando tarea: $item" }
        return  dbMongo.getCollection<Order>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Order): Boolean {
        logger.debug { "Eliminando tarea: $item" }
        return dbMongo.getCollection<Order>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Order> {
        logger.debug { "Recuperando todas las tareas "}
        return dbMongo.getCollection<Order>().find().toFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.debug { "Eliminando todas las tareas" }
        return dbMongo.getCollection<Order>().deleteMany("{}").wasAcknowledged()
    }
}