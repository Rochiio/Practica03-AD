package repositories.orders

import model.orders.Order
import repositories.ICRUD
import java.util.*

interface OrderRepository : ICRUD<Order, UUID>{
}