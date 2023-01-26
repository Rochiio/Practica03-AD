package controllers

import exception.CustomerErrorExists
import exception.CustomerErrorNotFound
import exception.CustomerSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import model.users.Customer
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import repositories.users.CustomerRepositoryImpl

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class CustomerControllerTest {

    @MockK
    private lateinit var repository: CustomerRepositoryImpl
    @InjectMockKs
    private lateinit var controller: CustomerController

    private var customer = Customer(name ="Cliente", username="Test", email ="email", password ="123456", available = true,
        orderList = emptyList(), tennisRacketsList = emptyList())


    @Test
    fun addCustomer() = runTest{
        coEvery { repository.findById(customer.id) } returns null
        coEvery { repository.save(customer) } returns customer

        val add = controller.addCustomer(customer)
        val resultSuccess = add as CustomerSuccess<Customer>

        assertAll(
            { assertTrue(resultSuccess.code==201) },
            { assertEquals(resultSuccess.data.id, customer.id) },
            { assertEquals(resultSuccess.data.name, customer.name) },
            { assertEquals(resultSuccess.data.username, customer.username) },
            { assertEquals(resultSuccess.data.email, customer.email) },
            { assertEquals(resultSuccess.data.password, customer.password) },
            { assertEquals(resultSuccess.data.available, customer.available) },
            { assertEquals(resultSuccess.data.orderList, customer.orderList) },
            { assertEquals(resultSuccess.data.tennisRacketsList, customer.tennisRacketsList) }
        )

        coVerify(exactly=1){repository.save(customer)}
        coVerify(exactly=1){repository.findById(customer.id)}
    }


    @Test
    fun addCustomerErrorYaExiste() = runTest{
        coEvery { repository.findById(customer.id) } returns customer

        val add = controller.addCustomer(customer)
        val resultError = add as CustomerErrorExists

        assertAll(
            { assertTrue(resultError.code==403) },
            { assertEquals(resultError.message, "Ya existe un cliente con el id: ${customer.id}") }
        )

        coVerify(exactly=1){repository.findById(customer.id)}
    }

//    TODO no lo realiza correctamente porq no puede similar el password parser (creo)
//    @Test
//    fun getCustomerByEmailAndPassword() =runTest{
//        coEvery { repository.findByEmail(customer.email) } returns customer
//
//        val get = controller.getCustomerByEmailAndPassword(customer.email, customer.password)
//        val resultSuccess = get as CustomerSuccess<Customer>
//
//        assertAll(
//            { assertTrue(resultSuccess.code==200) },
//            { assertEquals(resultSuccess.data.id, customer.id) },
//            { assertEquals(resultSuccess.data.name, customer.name) },
//            { assertEquals(resultSuccess.data.surname, customer.surname) },
//            { assertEquals(resultSuccess.data.email, customer.email) },
//            { assertEquals(resultSuccess.data.password, customer.password) },
//            { assertEquals(resultSuccess.data.available, customer.available) },
//            { assertEquals(resultSuccess.data.orderList, customer.orderList) },
//            { assertEquals(resultSuccess.data.tennisRacketsList, customer.tennisRacketsList) }
//        )
//
//        coVerify(exactly = 1){repository.findByEmail(customer.email)}
//    }


    @Test
    fun getCustomerByEmailAndPasswordError() =runTest{
        coEvery { repository.findByEmail(customer.email) } returns null

        val get = controller.getCustomerByEmailAndPassword(customer.email, customer.password)
        val resultSuccess = get as CustomerErrorNotFound

        assertAll(
            { assertTrue(resultSuccess.code==404) },
            { assertEquals(resultSuccess.message, "Usuario o contraseña incorrecto") }
        )

        coVerify(exactly = 1){repository.findByEmail(customer.email)}
    }


    @Test
    fun getCustomerById() =runTest{
        coEvery { repository.findById(customer.id) } returns customer
        val get = controller.getCustomerById(customer.id)
        val resultSuccess = get as CustomerSuccess<Customer>

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, customer.id) },
            { assertEquals(resultSuccess.data.name, customer.name) },
            { assertEquals(resultSuccess.data.username, customer.username) },
            { assertEquals(resultSuccess.data.email, customer.email) },
            { assertEquals(resultSuccess.data.password, customer.password) },
            { assertEquals(resultSuccess.data.available, customer.available) },
            { assertEquals(resultSuccess.data.orderList, customer.orderList) },
            { assertEquals(resultSuccess.data.tennisRacketsList, customer.tennisRacketsList) }
        )

        coVerify(exactly=1){repository.findById(customer.id)}
    }


    @Test
    fun getCustomerByIdError() =runTest{
        coEvery { repository.findById(customer.id) } returns null
        val get = controller.getCustomerById(customer.id)
        val resultError = get as CustomerErrorNotFound

        assertAll(
            { assertTrue(resultError.code==404) },
            { assertEquals(resultError.message, "No existe cliente con el id: ${customer.id}") }
        )

        coVerify(exactly=1){repository.findById(customer.id)}
    }

    @Test
    fun getAllCustomers() = runTest{
        coEvery { repository.findAll() } returns flowOf(customer)
        val get = controller.getAllCustomers()
        val result = get as CustomerSuccess<Flow<Customer>>
        val flow = result.data.toList()

        assertAll(
            { assertTrue(result.code==200) },
            { assertTrue(flow.isNotEmpty()) },
            { assertEquals(flow[0].id, customer.id) },
            { assertEquals(flow[0].name, customer.name) },
            { assertEquals(flow[0].username, customer.username) },
            { assertEquals(flow[0].email, customer.email) },
            { assertEquals(flow[0].password, customer.password) },
            { assertEquals(flow[0].available, customer.available) },
            { assertEquals(flow[0].orderList, customer.orderList) },
            { assertEquals(flow[0].tennisRacketsList, customer.tennisRacketsList) }
        )

        coVerify(exactly = 1){repository.findAll()}
    }

    @Test
    fun updateCustomer() = runTest{
        coEvery { repository.update(customer) } returns customer
        val get = controller.updateCustomer(customer)
        val resultSuccess = get as CustomerSuccess<Customer>

        assertAll(
            { assertTrue(resultSuccess.code==200) },
            { assertEquals(resultSuccess.data.id, customer.id) },
            { assertEquals(resultSuccess.data.name, customer.name) },
            { assertEquals(resultSuccess.data.username, customer.username) },
            { assertEquals(resultSuccess.data.email, customer.email) },
            { assertEquals(resultSuccess.data.password, customer.password) },
            { assertEquals(resultSuccess.data.available, customer.available) },
            { assertEquals(resultSuccess.data.orderList, customer.orderList) },
            { assertEquals(resultSuccess.data.tennisRacketsList, customer.tennisRacketsList) }
        )

        coVerify(exactly = 1){repository.update(customer)}
    }

    @Test
    fun deleteCustomer() = runTest{
        coEvery { repository.delete(customer) } returns true
        val get = controller.deleteCustomer(customer)
        val resultSuccess = get as CustomerSuccess<Boolean>

        assertAll(
            { assertTrue(resultSuccess.code == 200) },
            { assertTrue(resultSuccess.data) }
        )
    }
}