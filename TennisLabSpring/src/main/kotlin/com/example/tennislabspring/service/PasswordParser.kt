package com.example.tennislabspring.service

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

/**
 * Clase para el encriptado de la contraseña.
 */
object PasswordParser{

    /**
     * Para encriptar las contraseñas en SHA-512
     */
    fun encriptar(password:String):String{
        return Hashing.sha512().hashString(password,StandardCharsets.UTF_8).toString()
    }

}

