package interpreter.implementation

import common.ast.implementations.asts.BlockAST
import interpreter.input.Inputter
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope
import interpreter.output.Outputter

class BlockInterpreter(
    private val scope: Scope,
    private val inputter: Inputter,
    private val outputter: Outputter,
    private val booleanWrapper: BooleanWrapper
) : Interpreter<BlockAST> {
    override fun interpret(ast: BlockAST) {
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
