package repositories.users

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import model.users.Customer
import mu.KotlinLogging
import org.litote.kmongo.*
import java.util.*

/**
 * Implementación del repositorio de clientes.
 */
class CustomerRepositoryImpl : CustomerRepository {
    private var logger = KotlinLogging.logger {}
    private var dbMongo = MongoDbManager.database


    /**
     * Buscar un cliente por su correo.
     * @param email email por el que buscar.
     * @return el cliente si ha sido encontrado.
     */
    override suspend fun findByEmail(email: String): Customer? {
        logger.debug { "Buscando cliente con email: $email" }
        return dbMongo.getCollection<Customer>()
            .findOne(Customer::email eq email)
    }


    /**
     * Buscar un cliente por su uuid.
     * @param uuid uuid por el que buscar.
     * @return el cliente si ha sido encontrado.
     */
    override suspend fun findByUuid(uuid: UUID): Customer? {
        logger.debug { "Buscando cliente con uuid: $uuid" }
        return dbMongo.getCollection<Customer>()
            .findOne(Customer::uuid eq uuid)
    }


    /**
     * Buscar un cliente por su id.
     * @param id id del cliente a buscar.
     * @return el cliente si ha sido encontrado.
     */
    override suspend fun findById(id: String): Customer? {
        logger.debug { "Buscando cliente con id: $id"}
        return dbMongo.getCollection<Customer>()
            .findOneById(id)
    }


    /**
     * Salvar a un cliente.
     * @param item cliente a salvar.
     * @return cliente que ha sido salvado.
     */
    override suspend fun save(item: Customer): Customer {
        logger.debug { "Salvando cliente: $item" }
        return dbMongo.getCollection<Customer>()
            .save(item).let { item }
    }


    /**
     * Actualizar a un cliente.
     * @param item cliente a actualizar.
     * @return el cliente actualizado.
     */
    override suspend fun update(item: Customer): Customer {
        logger.debug { "Actualizando cliente: $item" }
        return dbMongo.getCollection<Customer>()
            .updateOneById(item.id, item).wasAcknowledged().let { item }
    }

    /**
     * TODO no se si este va a funcionar
     * Eliminar a un cliente.
     * @param item cliente a eliminar.
     * @return si el cliente ha sido eliminado.
     */
    override suspend fun delete(item: Customer): Boolean {
        logger.debug { "Eliminando cliente: $item" }
        return dbMongo.getCollection<Customer>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }


    /**
     * Buscar a todos los cliente que hay.
     * @return flujo de clientes.
     */
    override suspend fun findAll(): Flow<Customer> {
        logger.debug { "Buscando clientes" }
        return dbMongo.getCollection<Customer>().find().publisher.asFlow()
    }


    /**
     * Eliminar todos los clientes.
     * @return si han sido eliminados con éxito.
     */
    override suspend fun deleteAll(): Boolean {
        logger.debug { "Elminando todos los clientes" }
        return dbMongo.getCollection<Customer>()
            .deleteMany("{}").wasAcknowledged()
    }




}