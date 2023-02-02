package service.files

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.lists.CompleteOrdersList
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter


/**
 * Escirbir el fichero de pedidos completados en json.
 */
class JsonFilesCompleteOrders: JsonFiles<CompleteOrdersList> {
    private var json = Json { prettyPrint=true }
    private var mutex = Mutex()

    override suspend fun writeFichero(path: String, item: CompleteOrdersList) {
        var out: OutputStreamWriter
        var fichero = File(path)

        mutex.withLock {
            out = OutputStreamWriter(FileOutputStream(fichero, true))
            out.use { it.write(json.encodeToString(item)) }
        }
    }
}