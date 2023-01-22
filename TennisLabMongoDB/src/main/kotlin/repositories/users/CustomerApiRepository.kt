package repositories.users

import dto.customers.CustomerDTO
import model.users.Customer
import mu.KotlinLogging
import services.api.ApiUsersClient
import util.mappers.fromDto


class CustomerApiRepository {
    private val client by lazy { ApiUsersClient.instance }
    val logger = KotlinLogging.logger { }


    suspend fun findAll(page: Int, per_page: Int): List<CustomerDTO> {
        val call = client.getAll(page, per_page)
        try {
            logger.debug { "CustomerApiRepository - findAll - OK" }
            return call
        } catch (e: Exception) {

            throw Exception("CustomerApiRepository - findAll - ERROR - ${e.message}")
        }
    }

    suspend fun findById(id: Int): CustomerDTO? {

        logger.debug { "buscando cliente con id : $id" }
        val call = client.getById(id)

        try {
            logger.debug { "CustomerApiRepository - findById - OK" }
            return call
        } catch (e: Exception) {
            logger.debug { "CustomerApiRepository - findById - ERROR - ${e.message}" }
            return null
        }
    }

    suspend fun findByEmail(email: String): CustomerDTO? {
        logger.debug { "buscando cliente con email : $email" }
        val call = client.findByEmail(email)

        try {
            logger.debug { "CustomerApiRepository - findByEmail - OK " }
            return call
        } catch (e: Exception) {
            logger.debug { "CustomerApiRepository - findByEmail - ERROR - ${e.message}" }
            return null
        }
    }

    suspend fun save(customer: CustomerDTO): Customer {
        logger.debug { "guardando cliente : $customer" }
        try {
            val res = client.create(customer)
            logger.debug { "CustomerApiRepository - save - OK" }
            return res.fromDto()
        } catch (e: Exception) {
            logger.debug { "CustomerApiRepository - save - ERROR - ${e.message}" }
            throw Exception(e.message)
        }
    }

}