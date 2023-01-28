package util

import model.lists.StringerAssignments
import model.orders.Order
import model.orders.tasks.Task

/**
 * Datos necesarios para realizar distintas acciones.
 */
object Data {
    var assignments= mutableListOf<StringerAssignments>()
    var completeOrders = mutableListOf<Order>()
    var pendingOrders = mutableListOf<Order>()
    var tasksCreated = mutableListOf<Task>()
    var services =  listOf("Personalizacion","Encordado")
    var products = listOf("RAQUETA", "CORDAJE", "COMPLEMENTO")
}