package com.example.tennislabspring.service.files


interface JsonFiles<T>{
    suspend fun writeFichero(path: String, item: T)
}