package controllers

import com.mongodb.reactivestreams.client.ChangeStreamPublisher
import exception.OrderErrorExists
import exception.OrderErrorNotFound
import exception.OrderResult
import exception.OrderSuccess
import kotlinx.coroutines.flow.Flow
import model.machines.Customizer
import model.orders.Order
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import repositories.orders.OrderRepository
import service.reactive.Watchers

/**
 * Controlador de pedidos.
 */
@Single
class OrderController(private var repository: OrderRepository, private var watchers: Watchers) {


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
        val update = repository.update(pedido)
        return OrderSuccess(200, update)
    }


    /**
     * Elimina un pedido.
     * @param pedido pedido a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteOrder(pedido: Order): OrderResult<Boolean> {
        val delete = repository.delete(pedido)
        return OrderSuccess(200, delete)
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

    fun watchOrder() : ChangeStreamPublisher<Order> {
        return watchers.watchOrder()
    }
}