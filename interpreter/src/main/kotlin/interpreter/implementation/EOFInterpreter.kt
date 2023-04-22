package interpreter.implementation

import common.ast.AST
import interpreter.interfaces.Interpreter
import kotlin.system.exitProcess

class EOFInterpreter : Interpreter {
    override fun interpret(ast: AST) {
        exitProcess(0)
    }
}
