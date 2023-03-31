package parser

import java.io.File

object ResultOutputWriter {
    fun <T> writeListToFile(list: List<T>, fileName: String) {
        val file = File(fileName)
        file.writeText(list.joinToString("\n") { it.toString() })
    }
}
