package util.mappers

import dto.TaskDTO
import dto.customers.CustomerDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.orders.tasks.Task
import model.users.Customer

private val json = Json
fun CustomerDTO.fromDto(): Customer {
    return Customer(
        name = this.name.split(Regex.fromLiteral("[ -]"))[0],
        username = this.name,
        email = this.email,
        orderList = emptyList(),
        tennisRacketsList = emptyList(),
        available = true
    )
}

fun Task.toDto() : TaskDTO{
    return TaskDTO(
        completed = true,
        id = this.id.hashCode(),
        title = json.encodeToString(this),
        userId = this.idTrabajador.hashCode()
    )
}