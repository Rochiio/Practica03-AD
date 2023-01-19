package model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class Product(
    @BsonId @Contextual
    var id : Id<Product> = newId(),
    val type : TypeProduct,
    val brand : String,
    val model : String,
    var price : Float,
    var stock : Int
)


