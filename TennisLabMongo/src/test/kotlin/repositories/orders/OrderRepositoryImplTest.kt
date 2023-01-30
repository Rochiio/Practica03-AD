package repositories.orders

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.Status
import model.orders.Order
import model.orders.tasks.Task
import model.users.Customer
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class OrderRepositoryImplTest {
    private val repo = OrderRepositoryImpl()
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

    @Before
    fun setUp() = runTest {
        repo.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest {
        repo.deleteAll()
    }

    @Test
    fun findById() = runTest {
        repo.save(order)
        val res = repo.findById(order.id)

        assertAll(
            { assertNotNull(res) },
            { assertEquals(res?.id, order.id) },
            { assertEquals(res?.uuid, order.uuid) },
            { assertEquals(res?.state, order.state) },
            { assertEquals(res?.entryDate, order.entryDate) },
            { assertNull(res?.exitDate) },
            { assertNull(res?.finalDate) },
            { assertEquals(res?.maxDate, order.maxDate) },
            { assertEquals(res?.totalPrice, order.totalPrice) },
            { assertEquals(res?.client, order.client) },
            { assertEquals(res?.tasks, order.tasks) }
        )
    }

    @Test
    fun save() = runTest {
        val res = repo.save(order)
        assertAll(
            { assertEquals(res.id, order.id) },
            { assertEquals(res.uuid, order.uuid) },
            { assertEquals(res.state, order.state) },
            { assertEquals(res.entryDate, order.entryDate) },
            { assertNull(res.exitDate) },
            { assertNull(res.finalDate) },
            { assertEquals(res.maxDate, order.maxDate) },
            { assertEquals(res.totalPrice, order.totalPrice) },
            { assertEquals(res.client, order.client) },
            { assertEquals(res.tasks, order.tasks) }
        )


    }

    @Test
    fun update() = runTest {
        val save = repo.save(order)
        save.state = Status.TERMINADO
        val update = repo.update(save)
        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.uuid, save.uuid) },
            { assertEquals(update.state, save.state) },
            { assertEquals(update.entryDate, save.entryDate) },
            { assertNull(update.exitDate) },
            { assertNull(update.finalDate) },
            { assertEquals(update.maxDate, save.maxDate) },
            { assertEquals(update.totalPrice, save.totalPrice) },
            { assertEquals(update.client, save.client) },
            { assertEquals(update.tasks, save.tasks) }
        )
    }

    @Test
    fun delete() = runTest {
        val save = repo.save(order)
        val res = repo.delete(order)
        assertTrue(res)
    }

    @Test
    fun findAll() = runTest {
        repo.save(order)
        val res = repo.findAll().toList()
        assertAll(
            { assertTrue(res.isNotEmpty()) },
            { assertEquals(res.size, 1) },
            { assertEquals(res[0].id, order.id) },
            { assertEquals(res[0].uuid, order.uuid) },
            { assertEquals(res[0].state, order.state) },
            { assertEquals(res[0].entryDate, order.entryDate) },
            { assertNull(res[0].exitDate) },
            { assertNull(res[0].finalDate) },
            { assertEquals(res[0].maxDate, order.maxDate) },
            { assertEquals(res[0].totalPrice, order.totalPrice) },
            { assertEquals(res[0].client, order.client) },
            { assertEquals(res[0].tasks, order.tasks) }
        )
    }

    @Test
    fun deleteAll() = runTest {
        repo.save(order)
        val res = repo.deleteAll()
        assertTrue(res)
    }
}