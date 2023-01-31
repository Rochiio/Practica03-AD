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
@Named("TaskRepository")
class TaskRepositoryImpl : TaskRepository {
    private val dbMongo = MongoDbManager.database
    override suspend fun findById(id: String): Task? {
        logger.debug { "Buscando tarea con id: $id" }
        return dbMongo.getCollection<Task>().findOneById(id)
    }

    override suspend fun save(item: Task): Task {
        logger.debug { "Guardando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .save(item).let { item }
    }

    override suspend fun update(item: Task): Task {
        logger.debug { "Actualizando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Task): Boolean {
        logger.debug { "Eliminando tarea: $item" }
        return dbMongo.getCollection<Task>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Task> {
        logger.debug { "Recuperando todas las tareas " }
        return dbMongo.getCollection<Task>().find().toFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.debug { "Eliminando todas las tareas" }
        return dbMongo.getCollection<Task>().deleteMany("{}").wasAcknowledged()
    }
}