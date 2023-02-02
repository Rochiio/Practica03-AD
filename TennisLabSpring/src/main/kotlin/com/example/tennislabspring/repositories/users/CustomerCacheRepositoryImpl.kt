package com.example.tennislabspring.repositories.users

import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.service.cache.UsersCache
import mu.KotlinLogging
import org.springframework.stereotype.Repository

/**
 * Implementación del repositorio de caché de clientes.
 */
@Repository
class CustomerCacheRepositoryImpl: CustomerCacheRepository {
    private val logger = KotlinLogging.logger { }


    override suspend fun findById(id: String): Customer? {
        logger.info { "Cache --> Buscando por id en la caché" }
        return UsersCache.cache.get(id)
    }

    override suspend fun addCache(item: Customer): Customer {
        logger.info { "Cache --> Añadiendo cliente a la caché" }
        UsersCache.cache.put(item.id, item)
        return item
    }

    override suspend fun delete(item: Customer): Boolean {
        logger.info { "Cache --> Eliminando cliente de la caché" }
        UsersCache.cache.invalidate(item.id)
        return true
    }

    override suspend fun update(item: Customer): Customer {
        logger.info { "Cache --> Actualizando cliente de la caché" }
        UsersCache.cache.put(item.id, item)
        return item
    }


}