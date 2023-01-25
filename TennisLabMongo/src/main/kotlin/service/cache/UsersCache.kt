package service.cache

import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.users.Customer
import mu.KotlinLogging
import kotlin.time.Duration.Companion.minutes

/**
 * Cache de usuarios
 */
private val logger = KotlinLogging.logger {  }
private const val STOP = 6 * 10000L

object UsersCache {
    val cache = Cache.Builder()
        .expireAfterWrite(1.minutes)
        .build<String, Customer>()

    suspend fun refresh(){
        withContext(Dispatchers.IO) {
            launch {
                do {
                    logger.debug { "Limpiando caché 🗑" }
                    UsersCache.cache.invalidateAll()
                    delay(STOP)
                } while (true)
            }
        }
    }
}