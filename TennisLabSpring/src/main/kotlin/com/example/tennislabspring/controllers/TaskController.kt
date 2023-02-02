package com.example.tennislabspring.controllers


import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.orders.tasks.Task
import com.example.tennislabspring.repositories.orders.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Controlador de tareas.
 */
class TaskController(private var repository: TaskRepository, private var api: TasksApiRepository) {

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
        api.save(tarea)
        return TaskSuccess(201, tarea)
    }

    /**
     * Recupera todas las tareas.
     * @return Flujo de tareas
     */
    suspend fun getAllTasks(): TaskResult<Flow<Task>> {
        val flow = repository.findAll()
        return TaskSuccess(200, flow)
    }

    /**
     * Actualiza una tarea.
     * @param tarea tarea a actualizar
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateTask(tarea: Task): TaskResult<Task> {
        val update = repository.update(tarea)
        api.update(tarea)
        return TaskSuccess(200, update)
    }

    /**
     * Elimina un pedido.
     * @param task pedido a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteTask(task: Task): TaskResult<Boolean> {
        repository.delete(task)
        return TaskSuccess(200, true)
    }

    /**
     * Busca un pedido por su id.
     * @param id id de la tarea a buscar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun getTaskById(id: String): TaskResult<Task> {
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
    suspend fun addTaskToOrder(tarea: Task): TaskResult<Task> {
        when (getTaskById(tarea.id)) {
            is TaskError -> addTask(tarea)
            is TaskSuccess -> updateTask(tarea)
        }
        return TaskSuccess(204, tarea)
    }

    /**
     * Añade una tarea a la api remota
     * @param tarea tara que se va a guardar en la api remota
     * @return Result dependiendo del resultado de la acción
     */
    suspend fun saveTaskToRemote(task: Task): TaskResult<Task> {
        return try {
            api.save(task)
            TaskSuccess(200, task)
        } catch (e: Exception) {
            TaskErrorExists("Ya existe una tarea con este id")
        }
    }
}