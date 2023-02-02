package com.example.tennislabspring.model.lists

import com.example.tennislabspring.model.orders.Order
import kotlinx.serialization.Serializable

/**
 * Lista de pedidos pendientes, serializable para pasar a JSON.
 */
@Serializable
data class PendingOrdersList(
    private val orders: List<Order>
    ) {
}