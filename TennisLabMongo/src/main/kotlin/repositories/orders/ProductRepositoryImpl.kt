package repositories.orders

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import model.Product
import mu.KotlinLogging
import org.litote.kmongo.Id
import org.litote.kmongo.MongoOperator
import java.util.*

class ProductRepositoryImpl : ProductRepository {
    private var logger = KotlinLogging.logger {}
    private var dbMongo = MongoDbManager.database
    override suspend fun findById(id: Id<Product>): Product? {
        logger.debug { "Buscando producto con id: $id" }
        return dbMongo.getCollection<Product>().findOneById(id)
    }

    override suspend fun save(item: Product): Product {
        logger.debug { "Guardando producto: $item" }
        return dbMongo.getCollection<Product>()
            .save(item).let { item }
    }

    override suspend fun update(item: Product): Product {
        logger.debug { "Actualizando producto: $item" }
        return dbMongo.getCollection<Product>()
            .updateOneById(item.id, item)
            .wasAcknowledged().let { item }
    }

    override suspend fun delete(item: Product): Boolean {
        logger.debug { "Eliminando producto: $item" }
        return dbMongo.getCollection<Product>()
            .updateOneById(item.id, "{${MongoOperator.set}:{'available' : false}}").wasAcknowledged()
    }

    override suspend fun findAll(): Flow<Product> {
        logger.debug { "Recuperando todas las producto " }
        return dbMongo.getCollection<Product>().find().toFlow()
    }

    override suspend fun deleteAll(): Boolean {
        logger.debug { "Eliminando todas las producto" }
        return dbMongo.getCollection<Product>().deleteMany("{}").wasAcknowledged()
    }
}