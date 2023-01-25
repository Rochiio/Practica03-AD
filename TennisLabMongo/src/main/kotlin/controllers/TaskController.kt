package controller


import exception.*
import kotlinx.coroutines.flow.Flow
import model.orders.tasks.Task
import org.litote.kmongo.Id
import repositories.orders.TaskRepository



import java.util.*

class TaskController(private var repository: TaskRepository) {

    /**
     * Añade un pedido
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
     * Recupera todos los pedidos guardados. Una lista vacía si no hay pedidos
     */
    suspend fun getAllTasks(): TaskResult<Flow<Task>>{
        val flow = repository.findAll()
        return TaskSuccess(200, flow)
    }

    /**
     * Actualiza un pedido ya existente o lo guarda si no existe
     */
    suspend fun updateTask(tarea: Task): TaskResult<Task>{
        val update = repository.update(tarea)
        return TaskSuccess(200, update)
    }

    /**
     * Elimina un pedido
     */
    suspend fun deleteTask(task: Task): TaskResult<Boolean> {
        val delete = repository.delete(task)
        return TaskSuccess(200, delete)
    }

    /**
     * Busca un pedido por su UUID
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
     */
    suspend fun addTaskToOrder(tarea : Task) : TaskResult<Task>{
        when(getTaskById(tarea.id)){
            is TaskError -> addTask(tarea)
            is TaskSuccess ->  updateTask(tarea)
        }
        return TaskSuccess(204, tarea)
    }
}