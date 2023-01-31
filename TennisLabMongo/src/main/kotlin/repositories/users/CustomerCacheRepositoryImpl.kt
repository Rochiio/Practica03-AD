package repositories.users

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.users.Customer
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import service.cache.UsersCache

/**
 * Implementación del repositorio de caché de clientes.
 */
@Single
class CustomerCacheRepositoryImpl:  CustomerCacheRepository{
    private val logger = KotlinLogging.logger { }


    override suspend fun findById(id: String): Customer? {
        logger.debug { "Cache --> Buscando por id en la caché" }
        return UsersCache.cache.get(id)
    }

    override suspend fun addCache(item: Customer): Customer {
        logger.debug { "Cache --> Añadiendo cliente a la caché" }
        UsersCache.cache.put(item.id, item)
        return item
    }

    override suspend fun delete(item: Customer): Boolean {
        logger.debug { "Cache --> Eliminando cliente de la caché" }
        UsersCache.cache.invalidate(item.id)
        return true
    }

    override suspend fun update(item: Customer): Customer {
        logger.debug { "Cache --> Actualizando cliente de la caché" }
        UsersCache.cache.put(item.id, item)
        return item
    }


}