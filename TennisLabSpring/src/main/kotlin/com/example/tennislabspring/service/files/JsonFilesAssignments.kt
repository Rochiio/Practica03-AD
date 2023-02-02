package com.example.tennislabspring.service.files

import com.example.tennislabspring.model.lists.StringerAssignmentsList
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * Escribir el fichero de asignaciones en json.
 */
@Service
class JsonFilesAssignments: JsonFiles<StringerAssignmentsList> {
    private var json = Json { prettyPrint=true }
    private var mutex = Mutex()

    override suspend fun writeFichero(path: String, item: StringerAssignmentsList) {
        var out: OutputStreamWriter
        var fichero = File(path)

        mutex.withLock {
            out = OutputStreamWriter(FileOutputStream(fichero, true))
            out.use { it.write(json.encodeToString(item)) }
        }
    }
}