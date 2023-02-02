package com.example.tennislabspring.service.cache

import com.example.tennislabspring.model.users.Customer
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.springframework.stereotype.Service
import kotlin.time.Duration.Companion.minutes

/**
 * Cache de usuarios
 */
private val logger = KotlinLogging.logger { }
private const val STOP = 6 * 10000L

@Service
object UsersCache {
    val cache = Cache.Builder()
        .expireAfterWrite(1.minutes)
        .expireAfterAccess(1.minutes)
        .build<String, Customer>()

    suspend fun refresh() {
        withContext(newSingleThreadContext("cache")) {
            launch {
                println("ACTUALIZANDO CACHE")
                logger.info { "Limpiando caché 🗑" }
                cache.invalidateAll()
                delay(STOP)

            }
        }
    }
}