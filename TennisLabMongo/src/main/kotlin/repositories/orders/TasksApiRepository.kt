package repositories.orders

import dto.TaskDTO
import exception.TaskResult
import exception.TaskSuccess
import model.orders.tasks.Task
import mu.KotlinLogging
import service.api.ApiClient
import util.mappers.toDto

class TasksApiRepository {
    private val client by lazy { ApiClient.tasksInstance }
    private val logger = KotlinLogging.logger { }

    /**
     * Recupera las tareas de la api
     * @param page pagina de tareas
     * @param per_page cuantas tareas queremos por página
     * @return lista de tareas
     */
    suspend fun findAll(page: Int, per_page: Int): List<TaskDTO> {
        val call = client.getAll(page, per_page)
        return try {
            logger.debug { "TasksApiRepository - findall - OK" }
            call
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findAll - ERROR - ${e.message}" }
            emptyList()
        }
    }

    /**
     * Busca una tarea por su id
     * @param id id de la tarea
     * @return tarea con el id buscado o null si no existe
     */
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

    /**
     * Busca una tarea por el id del usuario que tiene asociado
     * @param id del usuario
     * @return la tarea con el usuario con el id o null si no existe
     */
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

    /**
     * Guarda una tarea en la api
     * @param tarea a guardar
     * @return la tarea guardada
     */
    suspend fun save(task: Task): Task {
        return try {
            val call = client.create(task.toDto())
            task
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findByUserId - ERROR - ${e.message}" }
            throw Exception(e.message)

        }
    }

    /**
     * Actualiza una tarea en la api
     * @param tarea que hay que actualizar
     * @return la tarea actualizada
     */
    suspend fun update(task: Task): Task {
        logger.debug { "actualizando tarea : $task" }
        try {

            val res = client.update(task._id, task.toDto())
            logger.debug { "CustomerApiRepository - update - OK" }
            return task

        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - update - ERROR - ${e.message}" }
            throw Exception(e.message)
        }
    }

}