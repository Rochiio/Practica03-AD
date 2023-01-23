package controllers

import exception.ProductErrorExists
import exception.ProductErrorNotFound
import exception.ProductResult
import exception.ProductSuccess
import kotlinx.coroutines.flow.Flow
import model.Product
import org.litote.kmongo.Id
import repositories.orders.ProductRepository

/**
 * Controlador de productos
 */
class ProductController(private var repository: ProductRepository) {

    /**
     * Añadir un producto
     */
    suspend fun addProduct(item: Product): ProductResult<Product> {
        val find = repository.findById(item.id)
        find?.let {
            return ProductErrorExists("Ya existe un producto con este id")
        }
        repository.save(item)
        return ProductSuccess(201, item)
    }


    /**
     * Buscar un producto por su uuid.
     */
    suspend fun getProductById(id: Id<Product>): ProductResult<Product> {
        val find = repository.findById(id)
        find?.let {
            return ProductSuccess(200, it)
        }
        return ProductErrorNotFound("No existe un producto con el id: $id")
    }


    /**
     * Actualizar un producto
     */
    suspend fun updateProduct(item: Product):  ProductResult<Product> {
        val update = repository.update(item)
        return ProductSuccess(200, update)
    }


    /**
     * Conseguir todos los productos.
     */
    suspend fun getAllProducts():  ProductResult<Flow<Product>> {
        val flow = repository.findAll()
        return ProductSuccess(200, flow)
    }


    /**
     * Eliminar un producto.
     */
    suspend fun deleteProduct(item: Product): ProductResult<Boolean> {
        val delete = repository.delete(item)
        return ProductSuccess(200, delete)
    }
}