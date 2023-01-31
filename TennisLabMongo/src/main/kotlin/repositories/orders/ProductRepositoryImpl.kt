package repositories.orders

import db.MongoDbManager
import kotlinx.coroutines.flow.Flow
import model.Product
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.MongoOperator
private var logger = KotlinLogging.logger {}

@Single
@Named("ProductRepository")
class ProductRepositoryImpl : ProductRepository {
    private var dbMongo = MongoDbManager.database
    override suspend fun findById(id: String): Product? {
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