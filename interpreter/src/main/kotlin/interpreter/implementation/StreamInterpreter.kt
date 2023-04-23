package interpreter.implementation

import common.providers.ast.ASTProvider
import interpreter.EmptyScope
import interpreter.Scope
import interpreter.interfaces.Interpreter

class StreamInterpreter(private val astProvider: ASTProvider) {
    private val interpreter = Interpreter(Scope(mutableMapOf(), mutableMapOf(), EmptyScope))
    fun interpret() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            interpreter.interpret(astProviderResult.get())
        }
        interpret()
    }
}
