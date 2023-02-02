package util

import model.lists.StringerAssignments
import model.orders.Order
import model.orders.tasks.Task
import java.io.File

/**
 * Datos necesarios para realizar distintas acciones.
 */
object Data {
    val DIR_JSON = System.getProperty("user.dir") + File.separator + "listados" + File.separator
    var assignments= mutableListOf<StringerAssignments>()
    var completeOrders = mutableListOf<Order>()
    var pendingOrders = mutableListOf<Order>()
    var tasksCreated = mutableListOf<Task>()
    var services =  listOf("Personalizacion","Encordado")
    var products = listOf("RAQUETA", "CORDAJE", "COMPLEMENTO")
}