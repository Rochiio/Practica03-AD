package com.example.tennislabspring.service.api

import com.example.tennislabspring.dto.customers.CustomerDTO
import com.example.tennislabspring.model.users.Customer
import de.jensklingenberg.ktorfit.http.*

interface ApiUsers {

    @GET("users/")
    suspend fun getAll(@Query("page") page: Int = 0, @Query("per_page") perPage: Int = 0): List<CustomerDTO>

    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Int): CustomerDTO

    //TODO(No está cogiendo los usuarios por email)
    @GET("users")
    suspend fun findByEmail(@Query("email") email: String): List<CustomerDTO>?

    @POST("users")
    suspend fun create(@Body user: CustomerDTO): CustomerDTO


    //TODO(Excepcion illegal input) ?? no entiendo
    @PUT("users/{id}")
    suspend fun update(@Path("id") id: Int, @Body customer: CustomerDTO): Customer

    /**
     * Elimina el cliente con el id dado de la api
     */
    @DELETE("users/{id}")
    suspend fun delete(@Path("id") id: Int)

}

