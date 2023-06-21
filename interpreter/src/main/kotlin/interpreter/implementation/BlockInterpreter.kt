package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.BlockAST
import common.io.Inputter
import common.io.Outputter
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope

class BlockInterpreter(private val scope: Scope, private val inputter: Inputter, private val outputter: Outputter, private val booleanWrapper: BooleanWrapper) : Interpreter {
    override fun interpret(ast: AST) {
        ast as BlockAST
        val interpreter = Interpreter(
            interpreter.Scope(mutableMapOf(), mutableMapOf(), scope),
            inputter,
            outputter,
            booleanWrapper
        )
        val astList = ast.getContainedASTs()
        astList.forEach { interpreter.interpret(it) }
    }
}
