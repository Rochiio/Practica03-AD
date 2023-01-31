package controllers

import exception.OrderError
import exception.OrderErrorExists
import exception.OrderErrorNotFound
import exception.OrderSuccess
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.Status
import model.orders.Order
import model.orders.tasks.Task
import model.users.Customer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import repositories.orders.OrderRepository
import repositories.orders.OrderRepositoryImpl
import java.time.LocalDate

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class OrderControllerTest {
    @MockK
    private lateinit var repository: OrderRepository

    @InjectMockKs
    private lateinit var controller: OrderController

    private var customer = Customer(
        name = "Cliente", username = "Test", email = "email", password = "123456", available = true,
        orderList = emptyList(), nId = 0, tennisRacketsList = emptyList()
    )

    private var order = Order(
        state = Status.EN_PROCESO,
        entryDate = LocalDate.now(),
        exitDate = null,
        finalDate = null,
        maxDate = LocalDate.now().plusDays(14),
        totalPrice = 10f,
        client = customer,
        tasks = emptyList<Task>()
    )

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun addOrder() = runTest {
        coEvery { repository.save(order) } returns order
        coEvery { repository.findById(order.id) } returns null
        val result = controller.addOrder(order)
        val res = result as OrderSuccess<Order>

        assertAll(
            { assertEquals(201, res.code) },
            { assertEquals(res.data.id, order.id) },
            { assertEquals(res.data.uuid, order.uuid) },
            { assertEquals(res.data.state, order.state) },
            { assertEquals(res.data.entryDate, order.entryDate) },
            { assertNull(res.data.exitDate) },
            { assertNull(res.data.finalDate) },
            { assertEquals(res.data.maxDate, order.maxDate) },
            { assertEquals(res.data.totalPrice, order.totalPrice) },
            { assertEquals(res.data.client, order.client) },
            { assertEquals(res.data.tasks, order.tasks) }
        )
    }

    @Test
    fun addOrderError() = runTest {
        //coEvery { repository.save(order) } returns order
        coEvery { repository.findById(order.id) } returns order
        val result = controller.addOrder(order)
        val res = result as OrderErrorExists

        assertAll(
            { assertEquals(403, res.code) },
            { assertEquals(res.message, "Ya existe un pedido con el mismo id") }
        )
    }

    @Test
    fun getOrders() = runTest {
        coEvery { repository.findAll() } returns flowOf(order)
        val result = controller.getOrders()
        val res = result as OrderSuccess
        val list = res.data.toList()
        assertAll(
            { assertTrue(res.code == 200) },
            { assertTrue(list.isNotEmpty()) }
        )
    }

    @Test
    fun updateOrder() = runTest {
        coEvery { repository.update(order) } returns order
        val result = controller.updateOrder(order)
        val res = result as OrderSuccess
        assertAll(
            { assertEquals(200, res.code) },
            { assertEquals(res.data.id, order.id) },
            { assertEquals(res.data.uuid, order.uuid) },
            { assertEquals(res.data.state, order.state) },
            { assertEquals(res.data.entryDate, order.entryDate) },
            { assertNull(res.data.exitDate) },
            { assertNull(res.data.finalDate) },
            { assertEquals(res.data.maxDate, order.maxDate) },
            { assertEquals(res.data.totalPrice, order.totalPrice) },
            { assertEquals(res.data.client, order.client) },
            { assertEquals(res.data.tasks, order.tasks) }
        )
    }

    @Test
    fun deleteOrder() = runTest {
        coEvery { repository.delete(order) } returns true
        val result = controller.deleteOrder(order)
        val res = result as OrderSuccess
        assertAll(
            { assertEquals(200, res.code) },
            { assertTrue(res.data) }
        )

    }

    @Test
    fun getOrderById() = runTest{
        coEvery { repository.findById(order.id) } returns order
        val result = controller.getOrderById(order.id)
        val res = result as OrderSuccess
        assertAll(
            { assertEquals(200, res.code) },
            { assertEquals(res.data.id, order.id) },
            { assertEquals(res.data.uuid, order.uuid) },
            { assertEquals(res.data.state, order.state) },
            { assertEquals(res.data.entryDate, order.entryDate) },
            { assertNull(res.data.exitDate) },
            { assertNull(res.data.finalDate) },
            { assertEquals(res.data.maxDate, order.maxDate) },
            { assertEquals(res.data.totalPrice, order.totalPrice) },
            { assertEquals(res.data.client, order.client) },
            { assertEquals(res.data.tasks, order.tasks) }
        )
    }

    @Test
    fun getOrderByIdError() = runTest {
        coEvery { repository.findById("") } returns null
        val result = controller.getOrderById("")
        val res = result as OrderErrorNotFound
        assertAll(
            { assertEquals(404, res.code) },
            { assertEquals(res.message, "No existe el pedido con este id") }
        )
    }
}