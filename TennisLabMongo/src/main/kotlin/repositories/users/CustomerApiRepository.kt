package repositories.users

import dto.customers.CustomerDTO
import model.users.Customer
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import service.api.ApiClient
import util.mappers.fromDto

@Single
@Named("CustomerApiRepository")
class CustomerApiRepository {
    private val client by lazy { ApiClient.usersInstance }
    private val logger = KotlinLogging.logger { }

    /**
     * Recupera los usuarios de la api
     * @param page pagina de tareas
     * @param per_page cuantas tareas queremos por página
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
     * Busca una usuario por su id
     * @param id id de la usuario
     * @return usuario con el id buscado o null si no existe
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
     * Busca un usuario por su email en la api
     * @param email el email del usuario
     * @return una lista con los usuario que comparten el email
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
     * Guarda una usuario en la api
     * @param usuario a guardar
     * @return la usuario guardada
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
     * Actualiza una usuario en la api
     * @param usuario que hay que actualizar
     * @return la usuario actualizada
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

    /**
     * elimina un usuario de la api
     * @param id id del usuario que vamos a eliminar
     */
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