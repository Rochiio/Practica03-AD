package com.example.tennislabspring.service.api

import com.example.tennislabspring.dto.TaskDTO
import de.jensklingenberg.ktorfit.http.*
interface ApiTasks {

    @GET("todos")
    suspend fun getAll(@Query("page") page: Int = 0, @Query("per_page") perPage: Int = 0): List<TaskDTO>

    @GET("todos/{id}")
    suspend fun getById(@Path("id") id: Int): TaskDTO?

    @GET("todos/{userId}")
    suspend fun getByUserId(@Path("userId") userId: Int): TaskDTO?

    @POST("todos")
    suspend fun create(@Body task: TaskDTO): TaskDTO

    @PUT("todos/{id}")
    suspend fun update(@Path("id") id: Int, @Body task: TaskDTO): TaskDTO

    @DELETE("todos/{id}")
    suspend fun delete(@Path("id") id: Int)

}