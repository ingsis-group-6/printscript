package formatter.implementations

import java.io.File
import kotlin.system.exitProcess

class FormattedTextWriter(private val fileToWrite: File) {

    private var tempFile = File("temp.ps")

    fun writeLine(line: String) {
        if (line === "EOF") {
            tempFile.copyTo(fileToWrite, overwrite = true)
            tempFile.delete()
            exitProcess(0)
        } else {
            tempFile.appendText(line)
        }
    }
}
