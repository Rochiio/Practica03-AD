package model.lists

import kotlinx.serialization.Serializable
import model.orders.Order

/**
 * Lista de pedidos pendientes, serializable para pasar a JSON.
 */
@Serializable
data class PendingOrdersList(
    private val orders: List<Order>
    ) {
}