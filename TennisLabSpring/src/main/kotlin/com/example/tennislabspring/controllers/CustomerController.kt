package com.example.tennislabspring.controllers

import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.repositories.users.CustomerRepository
import com.example.tennislabspring.service.PasswordParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.springframework.stereotype.Controller
import com.example.tennislabspring.repositories.users.CustomerCacheRepository
import org.springframework.beans.factory.annotation.Autowired

/**
 * Controlador de los Clientes.
 */
@Controller
class CustomerController
    @Autowired constructor(
    private var repository: CustomerRepository,
    private var cache: CustomerCacheRepository
) {
    private val logger = KotlinLogging.logger { }


    /**
     * Añadir un cliente.
     * @param cliente cliente a añadir a nuestros repositorios.
     * @return Result dependiendo de si ha sido correcta o no la acción.
     */
    suspend fun addCustomer(cliente: Customer): CustomerResult<Customer> {
        logger.debug { "Buscamos en la caché si existe el usuario" }
        val existeInCache = cache.findById(cliente.id)
        existeInCache?.let {
            return  CustomerErrorExists("Ya existe un cliente con el id: ${it.id}")
        }?: run{
            logger.debug { "Buscamos en el mongo si existe el usuario" }
            val existe = repository.findById(cliente.id)
            existe?.let {
                return  CustomerErrorExists("Ya existe un cliente con el id: ${it.id}")
            }?: run{
                logger.debug { "El usuario no existe --> creando y añadiendo a DB y Cache" }
                withContext(Dispatchers.IO) {
                    launch {
                        repository.save(cliente)
                    }
                    launch {
                        cache.addCache(cliente)
                    }
                }
                return CustomerSuccess<Customer>(201, cliente)
            }
        }
    }


    /**
     * Buscar un cliente por email y contraseña.
     * @param email correo del cliente.
     * @param password contraseña del cliente.
     * @return Result dependiendo de si se ha realizado correctamente la accion.
     */
    suspend fun getCustomerByEmailAndPassword(email: String, password: String): CustomerResult<Customer>{
        val find = repository.findCustomerByEmail(email)
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
     * @param id id del cliente a buscar.
     * @return Result dependiendo de si se ha realizado la accion correctamente.
     */
    suspend fun getCustomerById(id: String): CustomerResult<Customer>{
        logger.debug { "Buscando en la caché" }
        val findCache = cache.findById(id)
        findCache?.let {
            return CustomerSuccess(200, it)
        }?: run{
            val find = repository.findById(id)
            find?.let {
                cache.addCache(it)
                return CustomerSuccess(200, it)
            }
        }
        return CustomerErrorNotFound("No existe cliente con el id: $id")
    }

//      TODO no se si es necesario
//    /**
//     * Buscar un usuario por su uuid
//     */
//    suspend fun getCustomerByUuid(uuid: UUID): CustomerResult<Customer>{
//        logger.debug { "Buscamos en la cache si existe el usuario por su uuid" }
//        val findCache = UsersCache.cache.get(uuid)
//        findCache?.let {
//            return CustomerSuccess(200, it)
//        }?: run{
//            logger.debug { "Buscamos en la base de datos si existe el usuario por su id" }
//            val findDb = repository.findByUuid(uuid)
//            findDb?.let {
//                withContext(Dispatchers.IO){
//                    launch {
//                        UsersCache.cache.put(it.uuid, it)
//                    }
//                }
//                return CustomerSuccess(200, it)
//            }?:run{
//                return CustomerErrorNotFound("No existe cliente con el uuid: $uuid")
//            }
//        }
//    }


    /**
     * Conseguir todos los clientes que existen.
     * @return Result de flujo con los clientes.
     */
    suspend fun getAllCustomers():CustomerResult<Flow<Customer>>{
        val flow = repository.findAll()
        return CustomerSuccess<Flow<Customer>>(200, flow)
    }


    /**
     * Actualizar un cliente.
     * @param cliente cliente a actualizar.
     * @return Result dependiendo del resultado de la operacion.
     */
    suspend fun updateCustomer(cliente: Customer): CustomerResult<Customer>{
        withContext(Dispatchers.IO){
            launch {
                cache.update(cliente)
            }
        }

        val update = repository.save(cliente)
        return CustomerSuccess<Customer>(200, update)
    }


    /**
     * Eliminar un cliente
     * @param cliente cliente a eliminar.
     * @return Result dependiendo de si ha sido realizado correctamente la accion.
     */
    suspend fun deleteCustomer(cliente: Customer): CustomerResult<Boolean> {
        withContext(Dispatchers.IO) {
            launch {
                cache.delete(cliente)
            }
        }

        repository.delete(cliente)
        return CustomerSuccess<Boolean>(200, true)
    }


}