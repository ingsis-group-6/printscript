package printscript.v1.app

import interpreter.implementation.StreamInterpreter
import lexer.provider.FileTokenProvider
import parser.provider.ASTProvider
import java.io.File

fun main(){
    try {
        val fileName = "print-stream.ps"
        val tokenProvider = FileTokenProvider(File(fileName))
        val astProvider = ASTProvider(tokenProvider)
        val streamInterpreter = StreamInterpreter(astProvider)

        streamInterpreter.interpret()
    }
    catch (exception: Exception) {
        printInRed(exception)
    }
}
private fun printInRed(exception: Exception) = println("\u001B[31m${exception.message}\u001B[0m")
