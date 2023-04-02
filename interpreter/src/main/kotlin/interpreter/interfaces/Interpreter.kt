package interpreter.interfaces

import common.ast.AST

interface Interpreter {

    fun interpret(ast: AST)
}
