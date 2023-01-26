package service.files


interface JsonFiles<T>{
    suspend fun writeFichero(path: String, item: T)
}