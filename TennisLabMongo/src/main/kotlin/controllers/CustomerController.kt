package controllers

import exception.CustomerErrorExists
import exception.CustomerErrorNotFound
import exception.CustomerResult
import exception.CustomerSuccess
import kotlinx.coroutines.flow.Flow
import model.users.Customer
import org.litote.kmongo.Id
import repositories.users.CustomerRepository
import service.PasswordParser

class CustomerController(private var repository: CustomerRepository) {

    /**
     * Añadir un cliente
     */
    suspend fun addCustomer(cliente: Customer): CustomerResult<Customer> {
        val existe = repository.findById(cliente.id)
        existe?.let {
            return  CustomerErrorExists("Ya existe un cliente con el id: ${it.id}")
        }?: run{
            repository.save(cliente)
            return CustomerSuccess<Customer>(201, cliente)
        }
    }


    /**
     * Conseguir cliente por email y contraseña
     */
    suspend fun getCustomerByEmailAndPassword(email: String, password: String): CustomerResult<Customer>{
        val find = repository.findByEmail(email)
        find?.let {
            if (find.password != PasswordParser.encriptar(password)){
                return CustomerErrorNotFound("Usuario o contraseña incorrecto")
            }
        }?: run {
            return CustomerErrorNotFound("Usuario o contraseña incorrecto")
        }
        return CustomerSuccess<Customer>(200, find)
    }


    /**
     * Buscar un cliente por el id.
     */
    suspend fun getCustomerById(id: Id<Customer>): CustomerResult<Customer>{
        val find = repository.findById(id)
        find?.let {
           return CustomerSuccess(200, it)
        }
        return CustomerErrorNotFound("No existe cliente con el id: $id")
    }


    /**
     * Conseguir todos los clientes que existen.
     */
    suspend fun getAllCustomers():CustomerResult<Flow<Customer>>{
        val flow = repository.findAll()
        return CustomerSuccess<Flow<Customer>>(200, flow)
    }


    /**
     * Actualizar un cliente
     */
    suspend fun updateCustomer(cliente: Customer): CustomerResult<Customer>{
        val update = repository.update(cliente)
        return CustomerSuccess<Customer>(200, update)
    }


    /**
     * Eliminar un cliente
     */
    suspend fun deleteCustomer(cliente: Customer): CustomerResult<Boolean>{
        var delete = repository.delete(cliente)
        return CustomerSuccess<Boolean>(200, delete)
    }

}