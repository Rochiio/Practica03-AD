package service.files

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.lists.ListProductsServices
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * Escribir el fichero de productos y servicios en json.
 */
class JsonFilesProductsServices: JsonFiles<ListProductsServices> {
    private var json = Json { prettyPrint=true }
    private var mutex = Mutex()

    override suspend fun writeFichero(path: String, item: ListProductsServices) {
        var out: OutputStreamWriter
        var fichero = File(path)

        mutex.withLock {
            out = OutputStreamWriter(FileOutputStream(fichero, false))
            out.use { it.write(json.encodeToString(item))}
            out.close()
        }
    }
}