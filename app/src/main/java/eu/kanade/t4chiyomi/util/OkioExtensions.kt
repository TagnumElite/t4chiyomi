package eu.kanade.t4chiyomi.util

import okio.BufferedSource
import okio.Okio
import java.io.File
import java.io.OutputStream

/**
 * Saves the given source to a file and closes it. Directories will be created if needed.
 *
 * @param file the file where the source is copied.
 */
fun BufferedSource.saveTo(file: File) {
    try {
        // Create parent dirs if needed
        file.parentFile.mkdirs()

        // Copy to destination
        saveTo(file.outputStream())
    } catch (e: Exception) {
        close()
        file.delete()
        throw e
    }
}

/**
 * Saves the given source to an output stream and closes both resources.
 *
 * @param stream the stream where the source is copied.
 */
fun BufferedSource.saveTo(stream: OutputStream) {
    use { input ->
        Okio.buffer(Okio.sink(stream)).use {
            it.writeAll(input)
            it.flush()
        }
    }
}
