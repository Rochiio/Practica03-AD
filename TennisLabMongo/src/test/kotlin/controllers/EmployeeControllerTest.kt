package controllers

import exception.EmployeeErrorExists
import exception.EmployeeErrorNotFound
import exception.EmployeeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.users.Employee
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import repositories.users.EmployeeRepositoryImpl
import java.time.LocalDateTime

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class EmployeeControllerTest {
    @MockK
    private lateinit var repository: EmployeeRepositoryImpl
    @InjectMockKs
    private lateinit var controller: EmployeeController

    private var employee = Employee(name="Empleado", surname = "Test", email = "test", password ="1234", available = true,
        isAdmin = true, entryTime = LocalDateTime.now(), departureTime = LocalDateTime.now(), orderList = emptyList())


    @Test
    fun getCustomerByEmailAndPasswordError() =runTest{
        coEvery { repository.findByEmail(employee.email) } returns null

        val get = controller.getEmployeeByEmailAndPassword(employee.email, employee.password)
        val resultSuccess = get as EmployeeErrorNotFound

        assertAll(
            { assertTrue(resultSuccess.code==404) },
            { assertEquals(resultSuccess.message, "Usuario o contraseña incorrecto") }
        )

        coVerify(exactly = 1){repository.findByEmail(employee.email)}
    }

    @Test
    fun getEmployeeById() = runTest{
        coEvery { repository.findById(employee.id) } returns employee
        val get = controller.getEmployeeById(employee.id)
        val resultSuccess = get as EmployeeSuccess<Employee>

        assertAll(
            { assertTrue(resultSuccess.code == 200) },
            { assertEquals(resultSuccess.data.id, employee.id)},
            { assertEquals(resultSuccess.data.name, employee.name)},
            { assertEquals(resultSuccess.data.surname, employee.surname)},
            { assertEquals(resultSuccess.data.email, employee.email)},
            { assertEquals(resultSuccess.data.password, employee.password)},
            { assertEquals(resultSuccess.data.available, employee.available)},
            { assertEquals(resultSuccess.data.isAdmin, employee.isAdmin)},
            { assertEquals(resultSuccess.data.entryTime, employee.entryTime)},
            { assertEquals(resultSuccess.data.departureTime, employee.departureTime)},
            { assertEquals(resultSuccess.data.orderList, employee.orderList)}
        )

        coVerify(exactly = 1){repository.findById(employee.id)}
    }

    @Test
    fun getEmployeeByIdError() = runTest{
        coEvery { repository.findById(employee.id) } returns null
        val get = controller.getEmployeeById(employee.id)
        val resultSuccess = get as EmployeeErrorNotFound

        assertAll(
            { assertTrue(resultSuccess.code == 404) },
            { assertEquals(resultSuccess.message, "Empleado no encontrado con id: ${employee.id}")}
        )

        coVerify(exactly = 1){repository.findById(employee.id)}
    }

    @Test
    fun addEmployee() = runTest{
        coEvery { repository.findByEmail(employee.email) } returns null
        coEvery { repository.save(employee)} returns employee

        val get = controller.addEmployee(employee)
        val resultSuccess = get as EmployeeSuccess<Employee>

        assertAll(
            { assertTrue(resultSuccess.code == 201) },
            { assertEquals(resultSuccess.data.id, employee.id)},
            { assertEquals(resultSuccess.data.name, employee.name)},
            { assertEquals(resultSuccess.data.surname, employee.surname)},
            { assertEquals(resultSuccess.data.email, employee.email)},
            { assertEquals(resultSuccess.data.password, employee.password)},
            { assertEquals(resultSuccess.data.available, employee.available)},
            { assertEquals(resultSuccess.data.isAdmin, employee.isAdmin)},
            { assertEquals(resultSuccess.data.entryTime, employee.entryTime)},
            { assertEquals(resultSuccess.data.departureTime, employee.departureTime)},
            { assertEquals(resultSuccess.data.orderList, employee.orderList)}
        )

        coVerify(exactly = 1){repository.findByEmail(employee.email)}
        coVerify(exactly = 1){repository.save(employee)}
    }

    @Test
    fun addEmployeeError() = runTest{
        coEvery { repository.findByEmail(employee.email) } returns employee
        val get = controller.addEmployee(employee)
        val resultSuccess = get as EmployeeErrorExists

        assertAll(
            { assertTrue(resultSuccess.code == 403) },
            { assertEquals(resultSuccess.message, "Ya existe un trabajador con este email")}
        )

        coVerify(exactly = 1){repository.findByEmail(employee.email)}
    }

    @Test
    fun getAllEmployees() = runTest{
        coEvery { repository.findAll() } returns flowOf(employee)
        val get = controller.getAllEmployees()
        val result = get as EmployeeSuccess<Flow<Employee>>
        val flow = result.data.toList()

        assertAll(
            { assertTrue(result.code==200) },
            { assertTrue(flow.isNotEmpty()) },
            { assertEquals(flow[0].id, employee.id) },
            { assertEquals(flow[0].name, employee.name) },
            { assertEquals(flow[0].surname, employee.surname) },
            { assertEquals(flow[0].email, employee.email) },
            { assertEquals(flow[0].password, employee.password) },
            { assertEquals(flow[0].available, employee.available) },
            { assertEquals(flow[0].orderList, employee.orderList) },
            { assertEquals(flow[0].isAdmin, employee.isAdmin) },
            { assertEquals(flow[0].entryTime, employee.entryTime) },
            { assertEquals(flow[0].departureTime, employee.departureTime) },
            )

        coVerify(exactly = 1){repository.findAll()}
    }

    @Test
    fun updateEmployee() = runTest{
        coEvery { repository.update(employee) } returns employee
        val get = controller.updateEmployee(employee)
        val resultSuccess = get as EmployeeSuccess<Employee>

        assertAll(
            { assertTrue(resultSuccess.code == 200) },
            { assertEquals(resultSuccess.data.id, employee.id)},
            { assertEquals(resultSuccess.data.name, employee.name)},
            { assertEquals(resultSuccess.data.surname, employee.surname)},
            { assertEquals(resultSuccess.data.email, employee.email)},
            { assertEquals(resultSuccess.data.password, employee.password)},
            { assertEquals(resultSuccess.data.available, employee.available)},
            { assertEquals(resultSuccess.data.isAdmin, employee.isAdmin)},
            { assertEquals(resultSuccess.data.entryTime, employee.entryTime)},
            { assertEquals(resultSuccess.data.departureTime, employee.departureTime)},
            { assertEquals(resultSuccess.data.orderList, employee.orderList)}
        )

        coVerify(exactly = 1){repository.update(employee)}
    }

    @Test
    fun deleteEmployee() = runTest{
        coEvery { repository.delete(employee) } returns true
        val get = controller.deleteEmployee(employee)
        val resultSuccess = get as EmployeeSuccess<Boolean>

        assertAll(
            { assertTrue(resultSuccess.code == 200)},
            { assertTrue(resultSuccess.data)}
        )

        coVerify(exactly = 1){repository.delete(employee)}
    }
}