package interpreter.implementation

import common.ast.AST

class Interpreter: interpreter.interfaces.Interpreter {

    private val symbolTable: Map<String, Pair<String,String>> = emptyMap()
    override fun interpret(ast: AST) {
    }


}