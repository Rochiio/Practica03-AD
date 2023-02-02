package com.example.tennislabspring.controllers


import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.orders.Order
import com.example.tennislabspring.repositories.orders.OrderRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * Controlador de pedidos.
 */
@Controller
class OrderController
    @Autowired constructor(
        private var repository: OrderRepository) {


    /**
     * Añadir un pedido.
     * @param order a añadir.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun addOrder(order: Order): OrderResult<Order> {
        val find = repository.findById(order.id)
        find?.let {
            return OrderErrorExists("Ya existe un pedido con el mismo id")
        } ?: run {
            repository.save(order)
            return OrderSuccess(201, order)
        }
    }


    /**
     * Recupera todos los pedidos guardados.
     * @return Flujo de pedidos
     */
    suspend fun getOrders(): OrderResult<Flow<Order>>{
        val flow = repository.findAll()
        return OrderSuccess(200, flow)
    }


    /**
     * Actualiza un pedido.
     * @param pedido pedido a actualizar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateOrder(pedido: Order):OrderResult<Order>{
        val update = repository.save(pedido)
        return OrderSuccess(200, update)
    }


    /**
     * Elimina un pedido.
     * @param pedido pedido a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteOrder(pedido: Order): OrderResult<Boolean> {
        repository.delete(pedido)
        return OrderSuccess(200, true)
    }


    /**
     * Busca un pedido por su id
     * @param id id del pedido a buscar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun getOrderById(id: String): OrderResult<Order>{
        val find = repository.findById(id)
        find?.let {
            return OrderSuccess(200, it)
        }
        return OrderErrorNotFound("No existe el pedido con este id")
    }


}