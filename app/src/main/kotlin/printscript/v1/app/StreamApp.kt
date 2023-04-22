package printscript.v1.app

import formatter.implementations.FormattedTextWriter
import formatter.implementations.StreamedFormatter
import lexer.provider.FileTokenProvider
import parser.provider.ASTProvider
import java.io.File

fun main() {
    try {
        val fileName = "print-stream.ps"
        val tokenProvider = FileTokenProvider(File(fileName))
        val astProvider = ASTProvider(tokenProvider)

//        val streamInterpreter = StreamInterpreter(astProvider)
//        streamInterpreter.interpret()

        val ftw = FormattedTextWriter(File(fileName))
        val streamedFormatter = StreamedFormatter(astProvider, ftw, "formatter_config.json")
        streamedFormatter.format()

//        val streamedLinter = StreamedLinter(astProvider, "linter_config.json")
//        streamedLinter.lint()
    } catch (exception: Exception) {
        printInRed(exception)
    }
}
private fun printInRed(exception: Exception) = println("\u001B[31m${exception.message}\u001B[0m")
