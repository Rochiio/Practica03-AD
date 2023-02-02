package repositories.orders

import model.orders.Order
import org.litote.kmongo.Id
import repositories.ICRUD
import java.util.*

interface OrderRepository : ICRUD<Order, String>{
}