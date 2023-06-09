/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package printscript.v1.app

import common.token.Token
import common.token.TokenType
import lexer.implementation.Lexer
import java.io.File
import java.util.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("No function or source file was specified")
        exitProcess(0)
    }

    try {
        when (args[0].lowercase(Locale.getDefault())) {
            "lint" -> {
                val sourceFile = readSourceFile(args)
                runAppWithFunction(sourceFile, LinterFunction(args[2]))
            }
            "format" -> {
                val sourceFile = readSourceFile(args)
                runAppWithFunction(sourceFile, FormatFunction(sourceFile, args[2]))
            }
            "run" -> {
                val sourceFile = readSourceFile(args)
                runAppWithFunction(sourceFile, ExecuteFunction())
            }
            "help" -> printHelpMessage()
            else -> throw java.lang.Exception("Invalid function specified - use 'lint' , 'format', 'run', or help")
        }
    } catch (exception: Exception) {
        printInRed(exception)
    }
}

private fun readSourceFile(args: Array<String>): File {
    val sourceFile = File(args[1])
    if (!sourceFile.exists()) throw java.lang.Exception("File ${args[1]} does not exist.")
    return sourceFile
}

private fun printHelpMessage() {
    println("********** PRINTSCRIPT v1.0 **********")
    println("For execution, run with run [source-file] ")
    println("For formatting, run with format [source-file] [config-file]")
    println("For linting, run with lint [source-file] [config-file]")
}

private fun printInRed(exception: Exception) = println("\u001B[31m${exception.message}\u001B[0m")

private fun runAppWithFunction(file: File, function: PrintscriptFunction) {
    runLexer(file)

    val listOfTokensInLine = mutableListOf<Token>()
    val scanner = Scanner(File("Tokens.txt"))

    while (scanner.hasNextLine()) {
        val token = getTokenFromStringRepresentation(scanner.nextLine())
        listOfTokensInLine.add(token)

        if (token.tokenType == TokenType.SEMICOLON) {
            function.execute(listOfTokensInLine)
            listOfTokensInLine.clear()
        }
        if (!scanner.hasNextLine() && token.tokenType != TokenType.SEMICOLON) {
            throw java.lang.Exception("There is a semicolon missing in the last line of the file")
        }
    }
}

private fun runLexer(file: File) {
    val lexer = Lexer()
    lexer.extractTokensFromFile(file)
}

private fun getTokenFromStringRepresentation(input: String): Token {
    val parts = input.substringAfter("(").dropLast(1).split(", ")
    val orderId = parts[0].substringAfter("=").toInt()
    val tokenType = TokenType.valueOf(parts[1].substringAfter("="))
    val value = parts[2].substringAfter("=")
    val row = parts[3].substringAfter("=").toInt()
    val col = parts[4].substringAfter("=").toInt()

    return Token(orderId, tokenType, value, row, col)
}
