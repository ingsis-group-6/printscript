package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.BlockAST
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope

class BlockInterpreter(private val scope: Scope) : Interpreter {
    override fun interpret(ast: AST) {
        ast as BlockAST
        val interpreter = Interpreter(
            interpreter.Scope(mutableMapOf(), mutableMapOf(), scope)
        )
        val astList = ast.getContainedASTs()
        astList.forEach { interpreter.interpret(it) }
    }
}
