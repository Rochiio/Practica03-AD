package com.example.tennislabspring.controllers

import com.example.tennislabspring.exception.*
import com.example.tennislabspring.model.Product
import com.example.tennislabspring.repositories.orders.ProductRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller


/**
 * Controlador de productos
 */
@Controller
class ProductController
    @Autowired constructor(
        private var repository: ProductRepository) {

    /**
     * Añadir un producto.
     * @param item producto a añadir.
     * @return Result dependiendo del resultado de la accion.
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
     * Buscar un producto por su id.
     * @param id del producto a buscar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun getProductById(id: String): ProductResult<Product> {
        val find = repository.findById(id)
        find?.let {
            return ProductSuccess(200, it)
        }
        return ProductErrorNotFound("No existe un producto con este id")
    }


    /**
     * Actualizar un producto
     * @param item producto a actualizar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun updateProduct(item: Product):  ProductResult<Product> {
        val update = repository.save(item)
        return ProductSuccess(200, update)
    }


    /**
     * Conseguir todos los productos.
     * @return Flujo de productos
     */
    suspend fun getAllProducts():  ProductResult<Flow<Product>> {
        val flow = repository.findAll()
        return ProductSuccess(200, flow)
    }


    /**
     * Eliminar un producto.
     * @param item producto a eliminar.
     * @return Result dependiendo del resultado de la accion.
     */
    suspend fun deleteProduct(item: Product): ProductResult<Boolean> {
        repository.delete(item)
        return ProductSuccess(200, true)
    }

    suspend fun deleteAll(){
        repository.deleteAll()
    }
}