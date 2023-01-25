package controller


import exception.*
import kotlinx.coroutines.flow.Flow
import model.orders.tasks.Task
import org.litote.kmongo.Id
import repositories.orders.TaskRepository



import java.util.*

/**
 * Controlador de tareas.
 */
class TaskController(private var repository: TaskRepository) {

    /**
     * Añade una tarea.
     * @param tarea tarea a añadir.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun addTask(tarea: Task): TaskResult<Task> {
        val find = repository.findById(tarea.id)
        find?.let {
            return TaskErrorExists("Ya existe una tarea con este id")
        }
        repository.save(tarea)
        return TaskSuccess(201, tarea)
    }

    /**
     * Recupera todas las tareas.
     * @return Flujo de tareas
     */
    suspend fun getAllTasks(): TaskResult<Flow<Task>>{
        val flow = repository.findAll()
        return TaskSuccess(200, flow)
    }

    /**
     * Actualiza una tarea.
     * @param tarea tarea a actualizar
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateTask(tarea: Task): TaskResult<Task>{
        val update = repository.update(tarea)
        return TaskSuccess(200, update)
    }

    /**
     * Elimina un pedido.
     * @param task pedido a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteTask(task: Task): TaskResult<Boolean> {
        val delete = repository.delete(task)
        return TaskSuccess(200, delete)
    }

    /**
     * Busca un pedido por su id.
     * @param id id de la tarea a buscar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun getTaskById(id: String): TaskResult<Task>{
        val find = repository.findById(id)
        find?.let {
            return TaskSuccess(200, it)
        }
        return TaskErrorNotFound("No existe una tarea con el id: $id")
    }


    /**
     * Añadir una tarea que ha sido añadida a un pedido
     * @param tarea tarea que ha sido añadida a un pedido.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun addTaskToOrder(tarea : Task) : TaskResult<Task>{
        when(getTaskById(tarea.id)){
            is TaskError -> addTask(tarea)
            is TaskSuccess ->  updateTask(tarea)
        }
        return TaskSuccess(204, tarea)
    }
}