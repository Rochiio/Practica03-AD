package model.lists

import kotlinx.serialization.Serializable
import model.Product

/**
 * Lista de productos y servicios que ofremos para pasar a json.
 */
@Serializable
data class ProductsServicesList(
    val services: List<String>,
    val products: List<Product>
)
