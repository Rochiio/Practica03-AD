package com.example.tennislabspring

import com.example.tennislabspring.model.Racket
import com.example.tennislabspring.model.users.Customer
import com.example.tennislabspring.repositories.users.CustomerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class TennisLabSpringApplication
@Autowired constructor(
	private val repo: CustomerRepository
):CommandLineRunner {

	override fun run(vararg args: String?) = runBlocking{

		var r = Racket(brand = "prueba", model = "model", maneuverability = 2.5f, rigidity = 3.5f, balance = 3.6f)
		var c = Customer(name="Prueba", username = "username",email = "email",password ="1234", available = true, orderList = emptyList(),
			tennisRacketsList = listOf(r)
		)


		repo.save(c)

		println(c)
		println(r)

		exitProcess(0)
	}
}

fun main(args: Array<String>) {
	runApplication<TennisLabSpringApplication>(*args)
}
