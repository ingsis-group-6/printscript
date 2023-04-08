package printscript.v1.app

import common.token.Token
import formatter.implementations.Formatter
import interpreter.implementation.Interpreter
import linter.implementations.Linter
import parser.implementation.Parser

interface PrintscriptFunction {
    fun execute(tokenLine: List<Token>)
}

class ExecuteFunction : PrintscriptFunction {
    private val parser = Parser()
    private val interpreter = Interpreter()
    override fun execute(tokenLine: List<Token>) = interpreter.interpret(parser.parse(tokenLine))
}

class FormatFunction(configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val formatter = Formatter(configFileName)
    override fun execute(tokenLine: List<Token>) {
        formatter.format(parser.parse(tokenLine))
    }
}

class LinterFunction(configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val linter = Linter(configFileName)
    override fun execute(tokenLine: List<Token>) {
        linter.lint(parser.check(tokenLine))
    }
}
