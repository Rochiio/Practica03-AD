package repositories.orders

import model.Product
import org.litote.kmongo.Id
import repositories.ICRUD
import java.util.UUID

interface ProductRepository : ICRUD<Product, Id<Product>> {
}