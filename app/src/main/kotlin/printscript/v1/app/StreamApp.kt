package printscript.v1.app

import lexer.provider.FileTokenProvider
import java.io.File
import java.io.FileInputStream

fun main() {
//    try {
    val fileName = "print.ps"
    val tokenProvider = FileTokenProvider(FileInputStream(File(fileName)), "1.1")
//        val astProvider = ASTProvider(tokenProvider)
//
// //        val streamInterpreter = StreamInterpreter(astProvider)
// //        streamInterpreter.interpret()
//
//        val ftw = FormattedTextWriter(File(fileName))
//        val streamedFormatter = StreamedFormatter(astProvider, ftw, "formatter_config.json")
//        streamedFormatter.format()
//
// //        val streamedLinter = StreamedLinter(astProvider, "linter_config.json")
// //        streamedLinter.lint()
//    } catch (exception: Exception) {
//        printInRed(exception)
//    }

    var currentToken = tokenProvider.getToken()
    repeat(100) {
        println(currentToken)
        currentToken = tokenProvider.getToken()
    }
}
private fun printInRed(exception: Exception) = println("\u001B[31m${exception.message}\u001B[0m")
