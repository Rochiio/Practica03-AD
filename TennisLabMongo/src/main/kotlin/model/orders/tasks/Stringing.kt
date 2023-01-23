package model.orders.tasks

import kotlinx.serialization.Serializable
import model.Product
import util.serializer.UUIDSerializer
import java.util.*

data class Stringing(
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID = UUID.randomUUID(),
    var hTension : Int,
    var vTension : Int,
    var vString : Product,
    var hString : Product,
    var nKnots : Int,
    var price : Long,
    var racketId : String
)
