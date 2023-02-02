package repositories.orders

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import model.orders.Order
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.MongoOperator

private var logger = KotlinLogging.logger {}

@Single
class OrderRepositoryImpl : OrderRepository {
    val dbMongo = MongoDbManager.database
    override suspend fun findById(id: String): Order? {
        logger.info { "Buscando tarea con id: $id" }
        return dbMongo.getCollection<Order>().findOneById(id)
    }

    override suspend fun save(item: Order): Order {
        logger.info { "Guardando tarea: $item" }
        return dbMongo.getCollection<Order>()
            .save(item).let { item }
    }

    override suspend fun update(item: Order): Order {
        logger.info { "Actualizando tarea: $item" }
        return dbMongo.getCollection<Order>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Order): Boolean {
        logger.info { "Eliminando tarea: $item" }
        return dbMongo.getCollection<Order>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Order> {
        logger.info { "Recuperando todas las tareas " }
        return dbMongo.getCollection<Order>().find().toFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todas las tareas" }
        return dbMongo.getCollection<Order>().deleteMany("{}").wasAcknowledged()
    }
}