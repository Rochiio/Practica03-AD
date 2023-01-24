package repositories.orders

import model.orders.Order
import model.orders.tasks.Task
import org.litote.kmongo.Id
import repositories.ICRUD
import java.util.*

interface TaskRepository : ICRUD<Task, Id<Task>>{
}