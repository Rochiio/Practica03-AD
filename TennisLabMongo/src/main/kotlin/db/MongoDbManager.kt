package db

import mu.KotlinLogging
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.Properties

//TODO cambiar
private val logger = KotlinLogging.logger {}

// Si trabajas con corrutinas mira esto para optimizar los tipos a usar
// https://litote.org/kmongo/dokka/kmongo/org.litote.kmongo.coroutine/index.html

object MongoDbManager {
    private lateinit var mongoClient: CoroutineClient // Vamos ausar un wrapper  de //MongoClient
    lateinit var database: CoroutineDatabase// MongoDatabase

    // Podemos coger los datos de properties o de un fichero de configuración

    // Para Mongo Atlas
    private val properties = Properties()
    private const val MONGO_TYPE = "mongodb+srv://" //"mongodb://" // "mongodb+srv://"
    private const val HOST = "cluster0.pgdqg.mongodb.net" //"localhost" // "cluster0.pgdqg.mongodb.net"
    private const val PORT = 27017
    private const val DATABASE = "tenistas"
    private const val USERNAME = "mongo" //"mongoadmin"// "mongo"
    private const val PASSWORD = "XXXX"//"mongopass" //"xxxx"
    private const val OPTIONS = "?authSource=admin&retryWrites=true&w=majority"

    private const val MONGO_URI =
        "$MONGO_TYPE$USERNAME:$PASSWORD@$HOST/$DATABASE$OPTIONS"

    // Para local
    /*private const val MONGO_TYPE = "mongodb://" // "mongodb+srv://"
    private const val HOST = "localhost" // "cluster0.pgdqg.mongodb.net"
    private const val PORT = 27017
    private const val DATABASE = "tenistas"
    private const val USERNAME = "mongo"
    private const val PASSWORD = "mongo"
    private const val OPTIONS = "?retryWrites=true&w=majority"
    private const val MONGO_URI =
        "$MONGO_TYPE$HOST/$DATABASE"
     */

    init {
        logger.debug("Inicializando conexion a MongoDB")
        properties.load(javaClass.classLoader.getResourceAsStream("application.properties"))

        println(
            "Inicializando conexion a MongoDB -> ${properties.getProperty("mongo.conecction")}"
        )
        mongoClient =
            KMongo.createClient(properties.getProperty("mongo.conecction").toString())
                .coroutine
        database = mongoClient.getDatabase("tennisLab")
        /*mongoClient =
            KMongo.createClient("mongodb+srv://moha:1234@tennislabcluster.jgjryxb.mongodb.net/tennisLab")
                .coroutine
        database = mongoClient.getDatabase("tennislab")*/
    }
}