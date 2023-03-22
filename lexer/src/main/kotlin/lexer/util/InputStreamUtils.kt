package lexer.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class InputStreamUtils {
    companion object {
        fun convertStringToInputStream(input: String): InputStream =
            ByteArrayInputStream(input.toByteArray())

        fun convertFileToInputStream(input: File): InputStream =
            FileInputStream(input)
    }
}
