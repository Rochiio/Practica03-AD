package repositories.users

import dto.customers.CustomerDTO
import model.users.Customer
import mu.KotlinLogging
import services.api.ApiClient
import util.mappers.fromDto


class CustomerApiRepository {
    private val client by lazy { ApiClient.usersInstance }
    private val logger = KotlinLogging.logger { }

    /**
     * recupera todos los clientes desde la api
     */
    suspend fun findAll(page: Int, per_page: Int): List<CustomerDTO> {
        val call = client.getAll(page, per_page)
        try {
            logger.debug { "CustomerApiRepository - findAll - OK" }
            return call
        } catch (e: Exception) {
            logger.error { "Customer - findAll - ERROR - ${e.message}" }
            return emptyList()
        }
    }

    /**
     * busca un cliente por id en la api
     */
    suspend fun findById(id: Int): CustomerDTO? {

        logger.debug { "buscando cliente con id : $id" }
        val call = client.getById(id)

        return try {
            logger.debug { "CustomerApiRepository - findById - OK" }
            call
        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - findById - ERROR - ${e.message}" }
            null
        }
    }

    /**
     * busca un cliente por email en la api
     */
    suspend fun findByEmail(email: String): List<CustomerDTO>? {
        logger.debug { "buscando cliente con email : $email" }
        val call = client.findByEmail(email)
        return try {
            logger.debug { "CustomerApiRepository - findByEmail - OK " }
            call
        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - findByEmail - ERROR - ${e.message}" }
            null
        }
    }

    /**
     * guarda un elemento completo en la api
     */
    suspend fun save(customer: CustomerDTO): Customer {
        logger.debug { "guardando cliente : $customer" }
        try {
            val res = client.create(customer)
            logger.debug { "CustomerApiRepository - save - OK" }
            return res.fromDto()
        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - save - ERROR - ${e.message}" }
            throw Exception(e.message)
        }
    }

    /**
     * actualiza los campos de un cliente en la api
     */
    suspend fun update(customer: CustomerDTO): Customer {
        logger.debug { "actualizando cliente : $customer" }
        try {

            val res = client.update(customer.id, customer)
            logger.debug { "CustomerApiRepository - update - OK" }
            return res

        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - update - ERROR - ${e.message}" }
            throw Exception(e.message)
        }
    }

    suspend fun delete(id: Int) {
        logger.debug { "eliminando cliente con id : $id" }
        try {
            logger.debug { "CustomerApiRepository - delete - OK" }
            client.delete(id)
        } catch (e: Exception) {
            logger.debug { "CustomerApiRepository - delete - ERROR - ${e.message}" }
        }
    }

}