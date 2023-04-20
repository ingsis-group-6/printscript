package interpreter.implementation

import common.providers.ast.ASTProvider
import interpreter.interfaces.Interpreter

class StreamInterpreter(private val astProvider: ASTProvider) {
    private val interpreter = Interpreter()
    fun interpret() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            interpreter.interpret(astProviderResult.get())
        }
        interpret()
    }
}
