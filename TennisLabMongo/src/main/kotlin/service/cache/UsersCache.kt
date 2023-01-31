package service.cache

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.*
import model.users.Customer
import mu.KotlinLogging
import kotlin.time.Duration.Companion.minutes

/**
 * Cache de usuarios
 */
private val logger = KotlinLogging.logger { }
private const val STOP = 6 * 10000L

object UsersCache {
    val cache = Cache.Builder()
        .expireAfterWrite(1.minutes)
        .expireAfterAccess(1.minutes)
        .build<String, Customer>()

    suspend fun refresh() {
        withContext(newSingleThreadContext("cache")) {
            launch {
                println("hola")

                println("ACTUALIZANDO CACHE")
                logger.debug { "Limpiando caché 🗑" }
                cache.invalidateAll()
                delay(STOP)

            }
        }
    }
}