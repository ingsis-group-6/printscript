package lexer

import java.io.File

object ResultOutput {
    fun <T> writeListToFile(list: List<T>, fileName: String) {
        val file = File(fileName)
        file.writeText(list.joinToString("\n") { it.toString() })
    }

    fun <T> writeLineToFile(itemToWrite: T, fileName: String) {
        val file = File(fileName)
        file.appendText(itemToWrite.toString()+'\n')
    }
}
