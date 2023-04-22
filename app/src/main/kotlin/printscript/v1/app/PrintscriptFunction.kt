package printscript.v1.app

import common.token.Token
import formatter.implementations.Formatter
import interpreter.implementation.Interpreter
import linter.implementations.Linter
import parser.implementation.Parser
import java.io.File

interface PrintscriptFunction {
    fun execute(tokenLine: List<Token>)
}

class ExecuteFunction : PrintscriptFunction {
    private val parser = Parser()
    private val interpreter = Interpreter()
    override fun execute(tokenLine: List<Token>) = interpreter.interpret(parser.parse(tokenLine))
}

class LinterFunction(configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val linter = Linter(configFileName)
    override fun execute(tokenLine: List<Token>) {
        linter.lint(parser.check(tokenLine))
    }
}

class FormatFunction(fileToWrite: String, configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val formatter = Formatter(configFileName)
    private val ftw = FormattedTextWriter(File(fileToWrite))
    override fun execute(tokenLine: List<Token>) {
        ftw.writeLine(formatter.format(parser.parse(tokenLine)))
    }
}

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
