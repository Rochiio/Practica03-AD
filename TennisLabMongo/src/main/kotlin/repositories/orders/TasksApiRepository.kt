package repositories.orders

import dto.TaskDTO
import model.orders.tasks.Task
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import service.api.ApiClient
import util.mappers.toDto

private val logger = KotlinLogging.logger { }

@Single
class TasksApiRepository {
    private val client by lazy { ApiClient.tasksInstance }

    /**
     * Recupera las tareas de la api
     * @param page pagina de tareas
     * @param per_page cuantas tareas queremos por página
     * @return lista de tareas
     */
    suspend fun findAll(page: Int, per_page: Int): List<TaskDTO> {
        val call = client.getAll(page, per_page)
        return try {
            logger.info { "TasksApiRepository - findall - OK" }
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
        logger.info { "buscando tarea con id : $id" }
        val call = client.getById(id)

        return try {
            logger.info { "TasksApiRepository - findById - OK" }
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
        logger.info { "buscando tarea con id de usuario : $id" }
        val call = client.getByUserId(id)

        return try {
            logger.info { "TasksApiRepository - findByUserId - OK" }
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
            val a = task.toDto()
            println(a)
            val call = client.create(a)
            task
        } catch (e: Exception) {
            logger.error { "TasksApiRepository - findByUserId - ERROR - ${e.message}" }
            throw Exception("ERROR AL CREAR UNA TAREA")

        }
    }

    /**
     * Actualiza una tarea en la api
     * @param tarea que hay que actualizar
     * @return la tarea actualizada
     */
    suspend fun update(task: Task): Task {
        logger.info { "actualizando tarea : $task" }
        try {

            val res = client.update(task.nId, task.toDto())
            logger.info { "CustomerApiRepository - update - OK" }
            return task

        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - update - ERROR - ${e.message}" }
            throw Exception(e.message)
        }
    }

}