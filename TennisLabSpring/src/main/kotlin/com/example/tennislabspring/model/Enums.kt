package com.example.tennislabspring.model

/**
 * Tipos de máquinas que hay.
 */
enum class TypeMachine{
    AUTOMATICA, MANUAL
}


/**
 * Status en el que puede estar un pedido.
 */
enum class Status{
    RECIBIDO, EN_PROCESO, TERMINADO
}

/**
 * Tipos de tarea a realizar.
 */
enum class TypeTask{
    ENCORDADO, PERSONALIZADO, ADQUISICION
}


/**
 * Tipos de productos en venta.
 */
enum class TypeProduct{
    RAQUETA, CORDAJE, COMPLEMENTO
}
