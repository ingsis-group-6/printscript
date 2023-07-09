package formatter.implementations

import common.io.Outputter
import java.io.File

class FormattedTextWriter(private val fileToWrite: File) : Outputter {

    private var tempFile = File("temp.ps")

    override fun output(text: String) {
        if (text === "EOF") {
            tempFile.copyTo(fileToWrite, overwrite = true)
            tempFile.delete()
            // exitProcess(0)
        } else {
            tempFile.appendText(text)
        }
    }
}
