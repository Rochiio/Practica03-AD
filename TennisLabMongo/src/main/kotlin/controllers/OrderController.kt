package controllers

import exception.OrderErrorExists
import exception.OrderErrorNotFound
import exception.OrderResult
import exception.OrderSuccess
import kotlinx.coroutines.flow.Flow
import model.orders.Order
import org.litote.kmongo.Id
import repositories.orders.OrderRepository
import java.util.UUID


class OrderController(private var repository: OrderRepository) {


    /**
     * Añade un pedido
     */
    suspend fun addOrder(order: Order): OrderResult<Order> {
        val find = repository.findById(order.uuid)
        find?.let {
            return OrderErrorExists("Ya existe un pedido con el mismo id")
        } ?: run {
            repository.save(order)
            return OrderSuccess(201, order)
        }
    }


    /**
     * Recupera todos los pedidos guardados. Una lista vacía si no hay pedidos
     */
    suspend fun getOrders(): OrderResult<Flow<Order>>{
        val flow = repository.findAll()
        return OrderSuccess(200, flow)
    }


    /**
     * Actualiza un pedido ya existente o lo guarda si no existe
     */
    suspend fun updateOrder(pedido: Order):OrderResult<Order>{
        val update = repository.update(pedido)
        return OrderSuccess(200, update)
    }


    /**
     * Elimina un pedido
     */
    suspend fun deleteOrder(pedido: Order): OrderResult<Boolean> {
        val delete = repository.delete(pedido)
        return OrderSuccess(200, delete)
    }


    /**
     * Busca un pedido por su id
     */
    suspend fun getOrderById(id: UUID): OrderResult<Order>{
        val find = repository.findById(id)
        find?.let {
            return OrderSuccess(200, it)
        }
        return OrderErrorNotFound("No existe el pedido con este id")
    }


}