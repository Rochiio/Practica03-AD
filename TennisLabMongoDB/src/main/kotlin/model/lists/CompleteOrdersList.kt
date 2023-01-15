package model.lists

import kotlinx.serialization.Serializable
import model.orders.Order

/**
 * Listado de pedidos completados, serializable para pasar a JSON.
 */
@Serializable
data class CompleteOrdersList(
    val orders: List<Order>
)
