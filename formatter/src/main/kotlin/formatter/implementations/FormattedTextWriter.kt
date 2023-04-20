package formatter.implementations

import java.io.File

class FormattedTextWriter(private val fileToWrite: File) {

    private var hasStarted = false

    fun writeLine(line: String) {
        if (!hasStarted) {
            fileToWrite.writeText("")
            hasStarted = true
        }
        fileToWrite.appendText(line)
    }
}