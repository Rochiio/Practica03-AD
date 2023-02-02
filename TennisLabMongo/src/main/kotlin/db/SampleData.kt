package db

import model.Product
import model.TypeMachine
import model.TypeProduct
import model.machines.Customizer
import model.machines.Stringer
import model.users.Employee
import service.PasswordParser
import java.time.LocalDate

object SampleData {
    fun sampleProducts(): List<Product> = listOf(
        Product(
            type = TypeProduct.CORDAJE,
            brand = "Artengo",
            model = "CORDAJE DE TENIS MONOFILAMENTO NEGRO PENTAGONAL TA 930 SPIN grosor de 1,25 mm",
            price = 9.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.CORDAJE,
            brand = "Wilson",
            model = "CORDAJE DE TENIS WILSON MULTIFILAMENTOS SENSATION 1,35 mm BLANCO",
            price = 9.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.CORDAJE,
            brand = "Luxilon",
            model = "Cordaje Tenis Monofilamento Big Banger Alu Power Gris 1,25 mm Gris",
            price = 14.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.RAQUETA,
            brand = "Artengo",
            model = "Raqueta de tenis niños Artengo TR100 23",
            price = 11.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.RAQUETA,
            brand = "Babolat",
            model = "Raqueta de tenis Babolat Boost Rafa Nadal (260 gr)",
            price = 79.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.COMPLEMENTO,
            brand = "Babolat",
            model = "Raquetero Babolat pure azul 9R",
            price = 69.99f,
            stock = (1..100).random()
        ),
        Product(
            type = TypeProduct.COMPLEMENTO,
            brand = "Wilson",
            model = "Pelota de tenis Wilson US Open x4 velocidad",
            price = 6.99f,
            stock = (1..100).random()
        ),
    )


    fun sampleEmployees(): List<Employee> = listOf<Employee>(
        Employee(
            name = "admin",
            surname = "istrador",
            email = "admin@admin.com",
            password = PasswordParser.encriptar("admin"),
            available = true,
            isAdmin = true
        ),
        Employee(
            name = "rocio",
            surname = "palao",
            email = "rocio@mail.com",
            password = PasswordParser.encriptar("rocio"),
            available = true,
            isAdmin = false
        ),
        Employee(
            name = "mohamed",
            surname = "asidah",
            email = "moha@mail.com",
            password = PasswordParser.encriptar("moha"),
            available = true,
            isAdmin = false
        )
    )


    fun sampleStringers(): List<Stringer> =
        listOf<Stringer>(
            Stringer(
                acquisitionDate = LocalDate.parse("2015-11-15"),
                available = true,
                automatic = TypeMachine.MANUAL,
                brand = "Gamma Progression II",
                model = "602 Stringing Machine",
                maximumTension = 41,
                minimumTension = 4
            ),
            Stringer(
                acquisitionDate = LocalDate.parse("2019-05-24"),
                available = true,
                automatic = TypeMachine.AUTOMATICA,
                brand = "Gamma Progression II",
                model = "ELS",
                maximumTension = 43,
                minimumTension = 4
            )
        )


    fun sampleCustomizer(): List<Customizer> =
        listOf(
            Customizer(
                brand = "DKsportbot",
                model = "S616",
                acquisitionDate = LocalDate.parse("1998-01-03"),
                available = true,
                maneuverability = true,
                balance = true,
                rigidity = false
            ),
            Customizer(
                brand = "Gamma Progression II",
                model = "Customizer 3000",
                acquisitionDate = LocalDate.parse("2017-02-12"),
                available = true,
                maneuverability = true,
                balance = true,
                rigidity = true
            )
        )
}
