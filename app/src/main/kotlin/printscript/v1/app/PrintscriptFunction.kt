package printscript.v1.app

import common.token.Token
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

class FormatFunction : PrintscriptFunction {
    override fun execute(tokenLine: List<Token>) {
        TODO("Not yet implemented")
    }
}

class LinterFunction : PrintscriptFunction {
    private val parser = Parser()
    private val linter = Linter()
    override fun execute(tokenLine: List<Token>) {
        linter.lint(parser.parse(tokenLine))
    }
}
