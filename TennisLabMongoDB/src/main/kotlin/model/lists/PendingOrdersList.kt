package model.lists

import kotlinx.serialization.Serializable
import model.orders.Order

/**
 * Listado de pedidos pendientes, serializable para pasar a JSON.
 */
@Serializable
data class PendingOrdersList(
    val orders: List<Order>
)
