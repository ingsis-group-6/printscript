package interpreter.interfaces

import common.ast.implementations.asts.AST

interface Interpreter {

    fun interpret(ast: AST)
}
