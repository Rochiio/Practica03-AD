package com.example.tennislabspring.model.lists

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Lista de productos y servicios que ofremos para pasar a json.
 */
@Serializable
data class ListProductsServices(
    @SerialName("servicios")
    private val services: List<String>,
    @SerialName("productos")
    private val products: List<String>
){
}