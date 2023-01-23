package repositories.users

import dto.customers.CustomerDTO
import model.users.Customer
import mu.KotlinLogging
import services.api.ApiUsersClient
import util.mappers.fromDto


class CustomerApiRepository {
    private val client by lazy { ApiUsersClient.instance }
    val logger = KotlinLogging.logger { }

    /**
     * recupera todos los clientes desde la api
     */
    suspend fun findAll(page: Int, per_page: Int): List<CustomerDTO> {
        val call = client.getAll(page, per_page)
        try {
            logger.debug { "CustomerApiRepository - findAll - OK" }
            return call
        } catch (e: Exception) {

            throw Exception("CustomerApiRepository - findAll - ERROR - ${e.message}")
        }
    }

    /**
     * busca un cliente por id en la api
     */
    suspend fun findById(id: Int): CustomerDTO? {

        logger.debug { "buscando cliente con id : $id" }
        val call = client.getById(id)

        try {
            logger.debug { "CustomerApiRepository - findById - OK" }
            return call
        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - findById - ERROR - ${e.message}" }
            return null
        }
    }

    /**
     * busca un cliente por email en la api
      */
    suspend fun findByEmail(email: String): CustomerDTO? {
        logger.debug { "buscando cliente con email : $email" }
        val call = client.findByEmail(email)

        try {
            logger.debug { "CustomerApiRepository - findByEmail - OK " }
            return call
        } catch (e: Exception) {
            logger.error { "CustomerApiRepository - findByEmail - ERROR - ${e.message}" }
            return null
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

    suspend fun delete(id : Int){
        logger.debug { "eliminando cliente con id : $id" }
        try{
            logger.debug { "CustomerApiRepository - delete - OK" }
            client.delete(id)
        }catch(e : Exception){
            logger.debug { "CustomerApiRepository - delete - ERROR - ${e.message}" }
        }
    }

}