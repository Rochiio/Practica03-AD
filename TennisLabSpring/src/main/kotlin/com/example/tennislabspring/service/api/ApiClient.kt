package com.example.tennislabspring.service.api

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
object ApiClient {
    private val url = "https://jsonplaceholder.typicode.com/"

    private val ktorfit by lazy {
        // Podemos meterle flow directamente!!!
        // ktorfit.responseConverter(FlowResponseConverter())
        Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json { isLenient = true; ignoreUnknownKeys = true; prettyPrint = true })
                }
                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            }
            .baseUrl(url)
            .build()
    }

    // instancia para las llamadas de la api de usuarios
    val usersInstance by lazy {
        ktorfit.create<ApiUsers>()
    }

    // instancia para las llamadas a la api de tareas
    val tasksInstance by lazy{
        ktorfit.create<ApiTasks>()
    }
}
