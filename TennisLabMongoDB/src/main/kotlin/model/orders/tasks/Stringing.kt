package model.orders.tasks

import model.Product

data class Stringing(
    var hTension : Int,
    var vTension : Int,
    var vString : Product,
    var hString : Product,
    var nKnots : Int,
    var price : Long,
    var racketId : String
)
