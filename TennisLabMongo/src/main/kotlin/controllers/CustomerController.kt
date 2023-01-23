package controllers

import exception.CustomerErrorExists
import exception.CustomerErrorNotFound
import exception.CustomerResult
import exception.CustomerSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.users.Customer
import mu.KotlinLogging
import org.litote.kmongo.Id
import repositories.users.CustomerRepository
import service.PasswordParser
import service.cache.UsersCache
import java.util.*

class CustomerController(private var repository: CustomerRepository) {
    private val logger = KotlinLogging.logger { }

    /**
     * Añadir un cliente
     */
    suspend fun addCustomer(cliente: Customer): CustomerResult<Customer> {
        logger.debug { "Buscamos en la caché si existe el usuario" }
        val existeInCache = UsersCache.cache.get(cliente.uuid)
        existeInCache?.let {
            return  CustomerErrorExists("Ya existe un cliente con el id: ${it.id}")
        }?: run{
            logger.debug { "Buscamos en el mongo si existe el usuario" }
            val existe = repository.findById(cliente.id)
            existe?.let {
                return  CustomerErrorExists("Ya existe un cliente con el id: ${it.id}")
            }?: run{
                logger.debug { "El usuario no existe,creando y añadiendo a DB y Cache concurrentemente" }
                withContext(Dispatchers.IO) {
                    launch {
                        repository.save(cliente)
                    }
                    launch {
                        UsersCache.cache.put(cliente.uuid, cliente)
                    }
                }
                return CustomerSuccess<Customer>(201, cliente)
            }
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
     * Buscar un usuario por su uuid
     */
    suspend fun getCustomerByUuid(uuid: UUID): CustomerResult<Customer>{
        logger.debug { "Buscamos en la cache si existe el usuario por su uuid" }
        val findCache = UsersCache.cache.get(uuid)
        findCache?.let {
            return CustomerSuccess(200, it)
        }?: run{
            logger.debug { "Buscamos en la base de datos si existe el usuario por su id" }
            val findDb = repository.findByUuid(uuid)
            findDb?.let {
                withContext(Dispatchers.IO){
                    launch {
                        UsersCache.cache.put(it.uuid, it)
                    }
                }
                return CustomerSuccess(200, it)
            }?:run{
                return CustomerErrorNotFound("No existe cliente con el uuid: $uuid")
            }
        }
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