package repositories.orders

import dto.TaskDTO
import model.orders.tasks.Task
import mu.KotlinLogging
import services.api.ApiClient

class TasksApiRepository {
    private val client by lazy { ApiClient.tasksInstance }
    private val logger = KotlinLogging.logger { }

    suspend fun findAll(page: Int, per_page: Int): List<TaskDTO> {
        val call = client.getAll(page, per_page)
        try {
            logger.debug { "TasksApiRepository - findall - OK" }
            return call
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findAll - ERROR - ${e.message}" }
            return emptyList()
        }
    }

    suspend fun findBYId(id: Int): TaskDTO? {
        logger.debug { "buscando tarea con id : $id" }
        val call = client.getById(id)

        return try {
            logger.debug { "TasksApiRepository - findById - OK" }
            call
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findBYId - ERROR - ${e.message}" }
            null
        }
    }

    suspend fun findByUserId(id: Int): TaskDTO? {
        logger.debug { "buscando tarea con id de usuario : $id" }
        val call = client.getByUserId(id)

        return try {
            logger.debug { "TasksApiRepository - findByUserId - OK" }
            call
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findByUserId - ERROR - ${e.message}" }
            null
        }
    }



}