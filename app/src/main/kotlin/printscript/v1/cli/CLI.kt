package printscript.v1.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import common.token.Token
import common.token.TokenType
import interpreter.output.ConsolePrintOutputter
import lexer.implementation.Lexer
import printscript.v1.app.PrintscriptFunction
import printscript.v1.app.StreamedExecution
import printscript.v1.app.StreamedFormat
import printscript.v1.app.StreamedLint
import java.io.File
import java.io.FileInputStream
import java.util.*

class PrintScript : CliktCommand(name = "printscript", help = "Printscript") {
    override fun run() = Unit
}
class Run : CliktCommand(help = "Run a Printscript file") {

    private val sourceFile by argument(help = "The source file to run")
    private val version by argument(help = "Printscript version to run (optional)").optional()
    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), ExecuteFunction())
        println("Current version: $version")
        StreamedExecution(FileInputStream(File(sourceFile)), version.toString(), ConsolePrintOutputter()).execute()
    }
}

class Lint : CliktCommand(help = "Lint a Printscript file") {

    private val sourceFile by argument(help = "The source file to lint")
    private val configFile by argument(help = "The config file to use")
    private val version by argument(help = "Printscript version to run (optional)").optional()
    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), LinterFunction(configFile))
        println(File(sourceFile))
        StreamedLint(File(sourceFile), configFile, version.toString()).execute()
    }
}

class Format : CliktCommand(help = "Format a Printscript file") {

    private val sourceFile by argument(help = "The source file to format")
    private val configFile by argument(help = "The config file to use")
    private val version by argument(help = "Printscript version to run (optional)").optional()

    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), FormatFunction(sourceFile, configFile))
        StreamedFormat(File(sourceFile), configFile, version.toString()).execute()
    }
}

class CLIUtils {
    companion object {

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

        fun runAppWithFunction(file: File, function: PrintscriptFunction) {
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
    }
}
