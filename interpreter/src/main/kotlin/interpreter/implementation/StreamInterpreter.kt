package interpreter.implementation

import common.providers.ast.ASTProvider
import interpreter.EmptyScope
import interpreter.Scope
import interpreter.interfaces.Interpreter
import interpreter.output.Outputter

class StreamInterpreter(private val astProvider: ASTProvider, outputter: Outputter) {
    private val interpreter = Interpreter(Scope(mutableMapOf(), mutableMapOf(), EmptyScope), outputter)
    fun interpret() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            interpreter.interpret(astProviderResult.get())
        }
        interpret()
    }
}
