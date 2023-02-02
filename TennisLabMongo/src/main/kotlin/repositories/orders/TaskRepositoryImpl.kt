package repositories.orders

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import model.orders.tasks.Task
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.MongoOperator
import java.util.UUID

private var logger = KotlinLogging.logger {}

@Single
class TaskRepositoryImpl : TaskRepository {
    private val dbMongo = MongoDbManager.database
    override suspend fun findById(id: String): Task? {
        logger.info { "Buscando tarea con id: $id" }
        return dbMongo.getCollection<Task>().findOneById(id)
    }

    override suspend fun save(item: Task): Task {
        logger.info { "Guardando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .save(item).let { item }
    }

    override suspend fun update(item: Task): Task {
        logger.info { "Actualizando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Task): Boolean {
        logger.info { "Eliminando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Task> {
        logger.info { "Recuperando todas las tareas " }
        return dbMongo.getCollection<Task>().find().toFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.info { "Eliminando todas las tareas" }
        return dbMongo.getCollection<Task>().deleteMany("{}").wasAcknowledged()
    }
}