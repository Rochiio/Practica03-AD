package repositories.orders

import model.Product
import repositories.ICRUD
import java.util.UUID

interface ProductRepository : ICRUD<Product, UUID> {
}