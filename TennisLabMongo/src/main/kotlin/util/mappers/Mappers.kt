
package util.mappers

import dto.TaskDTO
import dto.customers.CustomerDTO
import model.orders.tasks.Task
import model.users.Customer

fun CustomerDTO.fromDto(): Customer {
    return Customer(
        name = this.name,
        username = this.name,
        email = this.email,
        orderList = emptyList(),
        tennisRacketsList = emptyList(),
        available = true,
        _id = id
    )
}

fun Task.toDto() : TaskDTO = TaskDTO(
    id = this._id,
    title = this.description,
    userId = 0,
    completed = available
)