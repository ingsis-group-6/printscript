package interpreter.implementation

import common.ast.AST
import interpreter.interfaces.Interpreter

class EOFInterpreter(private var isEOF: BooleanWrapper) : Interpreter {
    override fun interpret(ast: AST) {
        isEOF.setValue(true)
    }
}
