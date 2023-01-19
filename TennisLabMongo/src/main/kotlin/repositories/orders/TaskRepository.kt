package repositories.orders

import model.orders.tasks.Task
import repositories.ICRUD
import java.util.*

interface TaskRepository : ICRUD<Task, UUID>{
}