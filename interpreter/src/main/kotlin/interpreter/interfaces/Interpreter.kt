package interpreter.interfaces

import common.ast.implementations.asts.AST

interface Interpreter<T: AST> {

    fun interpret(ast: T)
}
