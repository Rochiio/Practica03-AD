package repositories.users

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.users.Employee
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class EmployeeRepositoryImplTest {
    private var repo = EmployeeRepositoryImpl()
    private var employee = Employee(name="Empleado", surname = "Test", email = "test", password ="1234", available = true,
        isAdmin = true, entryTime = LocalDateTime.now(), departureTime = LocalDateTime.now(), orderList = mutableListOf()
    )

    @Before
    fun setUpEach() = runTest{
        repo.deleteAll()
    }

    @AfterEach
    fun tearDown() = runTest{
        repo.deleteAll()
    }

    @Test
    fun findByEmail() = runTest{
        repo.save(employee)
        val find = repo.findByEmail(employee.email)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, employee.id) },
            { assertEquals(find?.name, employee.name) },
            { assertEquals(find?.surname, employee.surname) },
            { assertEquals(find?.email, employee.email) },
            { assertEquals(find?.password, employee.password) },
            { assertEquals(find?.available, employee.available) },
            { assertEquals(find?.isAdmin, employee.isAdmin) },
            { assertEquals(find?.orderList, employee.orderList) },
        )
    }

    @Test
    fun findById() = runTest{
        repo.save(employee)
        val find = repo.findById(employee.id)

        assertAll(
            { assertNotNull(find) },
            { assertEquals(find?.id, employee.id) },
            { assertEquals(find?.name, employee.name) },
            { assertEquals(find?.surname, employee.surname) },
            { assertEquals(find?.email, employee.email) },
            { assertEquals(find?.password, employee.password) },
            { assertEquals(find?.available, employee.available) },
            { assertEquals(find?.isAdmin, employee.isAdmin) },
            { assertEquals(find?.orderList, employee.orderList) },
        )
    }

    @Test
    fun save() = runTest{
        val save = repo.save(employee)

        assertAll(
            { assertEquals(save.id, employee.id) },
            { assertEquals(save.name, employee.name) },
            { assertEquals(save.surname, employee.surname) },
            { assertEquals(save.email, employee.email) },
            { assertEquals(save.password, employee.password) },
            { assertEquals(save.available, employee.available) },
            { assertEquals(save.isAdmin, employee.isAdmin) },
            { assertEquals(save.orderList, employee.orderList) },
        )
    }

    @Test
    fun update() =runTest{
        val save = repo.save(employee)
        save.email="emailnuevo@gmail"
        val update = repo.update(save)

        assertAll(
            { assertEquals(update.id, save.id) },
            { assertEquals(update.name, save.name) },
            { assertEquals(update.surname, save.surname) },
            { assertEquals(update.email, save.email) },
            { assertEquals(update.password, save.password) },
            { assertEquals(update.available, save.available) },
            { assertEquals(update.isAdmin, save.isAdmin) },
            { assertEquals(update.orderList, save.orderList) },
        )
    }

    @Test
    fun delete() =runTest{
        repo.save(employee)
        val delete = repo.delete(employee)
        assertTrue(delete)
    }

    @Test
    fun findAll() =runTest{
        repo.save(employee)
        var all = repo.findAll().toList()

        assertAll(
            { assertTrue(all.isNotEmpty()) },
            { assertEquals(all[0].id, employee.id) },
            { assertEquals(all[0].name, employee.name) },
            { assertEquals(all[0].surname, employee.surname) },
            { assertEquals(all[0].email, employee.email) },
            { assertEquals(all[0].password, employee.password) },
            { assertEquals(all[0].available, employee.available) },
            { assertEquals(all[0].isAdmin, employee.isAdmin) },
            { assertEquals(all[0].orderList, employee.orderList) },
        )
    }

    @Test
    fun deleteAll() =runTest{
        repo.save(employee)
        val delete = repo.deleteAll()
        assertTrue(delete)
    }
}