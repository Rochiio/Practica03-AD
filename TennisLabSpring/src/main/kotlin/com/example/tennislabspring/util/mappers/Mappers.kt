package com.example.tennislabspring.util.mappers

import com.example.tennislabspring.dto.TaskDTO
import com.example.tennislabspring.dto.customers.CustomerDTO
import com.example.tennislabspring.model.orders.tasks.Task
import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.service.PasswordParser

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