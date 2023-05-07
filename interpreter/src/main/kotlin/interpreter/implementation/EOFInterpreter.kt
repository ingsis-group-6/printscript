package interpreter.implementation

import common.ast.implementations.asts.AST
import common.ast.implementations.asts.EndOfFileAST
import interpreter.interfaces.Interpreter

class EOFInterpreter(private var isEOF: BooleanWrapper) : Interpreter<EndOfFileAST> {
    override fun interpret(ast: EndOfFileAST) {
        isEOF.setValue(true)
    }
}
