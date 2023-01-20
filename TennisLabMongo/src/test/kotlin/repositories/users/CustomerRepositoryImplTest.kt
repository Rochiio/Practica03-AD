package repositories.users

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import model.users.Customer
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class CustomerRepositoryImplTest {
    private var repo = CustomerRepositoryImpl()
    private var customer = Customer(name ="Cliente", surname="Test", email ="email", password ="123456", available = true,
        orderList = emptyList(), tennisRacketsList = emptyList())

    private val mainThreadSurrogate = newSingleThreadContext("Test thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @BeforeEach
    fun setUpEach() = runTest{
        repo.deleteAll()
    }

    @After
    fun tearDown() = runTest{
        repo.deleteAll()
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }


    @Test
    fun findByEmail() = runTest{
        repo.save(customer)
        val find = repo.findByEmail(customer.email)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, customer.id) },
            { assertEquals(find?.name, customer.name) },
            { assertEquals(find?.surname, customer.surname) },
            { assertEquals(find?.email, customer.email) },
            { assertEquals(find?.password, customer.password) },
            { assertEquals(find?.available, customer.available) },
            { assertEquals(find?.orderList, customer.orderList) },
            { assertEquals(find?.tennisRacketsList, customer.tennisRacketsList) }
        )
    }

    @Test
    fun findById() = runTest{
        repo.save(customer)
        val find = repo.findById(customer.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, customer.id) },
            { assertEquals(find?.name, customer.name) },
            { assertEquals(find?.surname, customer.surname) },
            { assertEquals(find?.email, customer.email) },
            { assertEquals(find?.password, customer.password) },
            { assertEquals(find?.available, customer.available) },
            { assertEquals(find?.orderList, customer.orderList) },
            { assertEquals(find?.tennisRacketsList, customer.tennisRacketsList) }
        )
    }

    @Test
    fun save() = runTest{
        val save = repo.save(customer)

        assertAll(
            { assertEquals(save.id, customer.id) },
            { assertEquals(save.name, customer.name) },
            { assertEquals(save.surname, customer.surname) },
            { assertEquals(save.email, customer.email) },
            { assertEquals(save.password, customer.password) },
            { assertEquals(save.available, customer.available) },
            { assertEquals(save.orderList, customer.orderList) },
            { assertEquals(save.tennisRacketsList, customer.tennisRacketsList) }
        )
    }

    @Test
    fun update() = runTest{
        val save = repo.save(customer)
        save.email="nuevoemail@gmail.com"
        val update = repo.update(save)

        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.name, save.name) },
            { assertEquals(update.surname, save.surname) },
            { assertEquals(update.email, save.email) },
            { assertEquals(update.password, save.password) },
            { assertEquals(update.available, save.available) },
            { assertEquals(update.orderList, save.orderList) },
            { assertEquals(update.tennisRacketsList, save.tennisRacketsList) }
        )
    }

    @Test
    fun delete() = runTest{
        repo.save(customer)
        var delete = repo.delete(customer)
        assertTrue(delete)
    }

    @Test
    fun findAll() = runTest{
        repo.save(customer)
        var all = repo.findAll().toList()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(all[0].id, customer.id) },
            { assertEquals(all[0].name, customer.name) },
            { assertEquals(all[0].surname, customer.surname) },
            { assertEquals(all[0].email, customer.email) },
            { assertEquals(all[0].password, customer.password) },
            { assertEquals(all[0].available, customer.available) },
            { assertEquals(all[0].orderList, customer.orderList) },
            { assertEquals(all[0].tennisRacketsList, customer.tennisRacketsList) }
        )
    }

    @Test
    fun deleteAll() = runTest{
        repo.save(customer)
        val delete = repo.deleteAll()
        assertTrue(delete)
    }

}