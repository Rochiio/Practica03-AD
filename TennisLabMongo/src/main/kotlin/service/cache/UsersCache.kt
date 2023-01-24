package service.cache

import io.github.reactivecircus.cache4k.Cache
import model.users.Customer
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

/**
 * Cache de usuarios
 */
object UsersCache {
    val cache = Cache.Builder()
        .expireAfterWrite(1.minutes)
        .build<UUID, Customer>()
}