package util.mappers

import dto.TaskDTO
import dto.customers.CustomerDTO
import model.orders.tasks.Task
import model.users.Customer
import service.PasswordParser

fun CustomerDTO.fromDto(): Customer {
    return Customer(
        name = this.name,
        username = this.name,
        email = this.email,
        orderList = emptyList(),
        tennisRacketsList = emptyList(),
        available = true,
        password = PasswordParser.encriptar("1234"),
        nId = id
    )
}

fun Task.toDto(): TaskDTO {

    val res = TaskDTO(
        id = this.nId,
        title = this.id,
        userId = 0,
        completed = available
    )
    println(res)
    return res
}