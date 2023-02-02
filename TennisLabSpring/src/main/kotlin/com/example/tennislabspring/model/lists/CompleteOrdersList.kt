package com.example.tennislabspring.model.lists

import com.example.tennislabspring.model.orders.Order
import kotlinx.serialization.Serializable

/**
 * Listado de pedidos completados, serializable para pasar a JSON.
 */
@Serializable
data class CompleteOrdersList(
    private val orders: List<Order>
) {
}