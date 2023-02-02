package com.example.tennislabspring.controller

import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.repositories.users.CustomerRepository
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class CustomerController
@Autowired constructor(
    private val repository : CustomerRepository
){
    suspend fun getByEmail(email : String) : List<Customer>{
        println("Buscando $email")
        val find = repository.findByEmail(email)
//        println(find.toList())
        return find.toList()
    }

    suspend fun save(c : Customer) : Customer?{
        return repository.save(c)
    }
}