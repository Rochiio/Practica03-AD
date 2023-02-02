package db

import com.mongodb.MongoClientSettings
import mu.KotlinLogging
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.Properties
import java.util.logging.Level
import java.util.logging.Logger


private val logger = KotlinLogging.logger {  }

object MongoDbManager {
    private lateinit var mongoClient: CoroutineClient
    lateinit var database: CoroutineDatabase

    private val properties = Properties()

    init {

        logger.info("Inicializando conexion a MongoDB")
        properties.load(javaClass.classLoader.getResourceAsStream("application.properties"))
        if (properties.getProperty("mongo.local").toBoolean()) {
            println(
                "Inicializando conexion a MongoDB -> ${properties.getProperty("mongo.conection.local")}"
            )
            mongoClient =
                KMongo.createClient(properties.getProperty("mongo.conection.local").toString())
                    .coroutine


        } else {
            println(
                "Inicializando conexion a MongoDB -> ${properties.getProperty("mongo.conection")}"
            )
            mongoClient =
                KMongo.createClient(properties.getProperty("mongo.conection").toString())
                    .coroutine
        }
        database = mongoClient.getDatabase("tennisLab")

    }
}