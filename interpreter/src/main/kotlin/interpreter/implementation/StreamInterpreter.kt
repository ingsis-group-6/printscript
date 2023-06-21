package interpreter.implementation

import common.providers.ast.ASTProvider
import interpreter.EmptyScope
import interpreter.Scope
import common.io.Inputter
import common.io.Outputter

class StreamInterpreter(private val astProvider: ASTProvider, inputter: Inputter, outputter: Outputter) {
    private var isEOF = BooleanWrapper(false)
    private val interpreter = Interpreter(Scope(mutableMapOf(), mutableMapOf(), EmptyScope), inputter, outputter, isEOF)

    tailrec fun interpret() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            interpreter.interpret(astProviderResult.get())
        }
        if (!isEOF.booleanValue) interpret()
    }
}

class BooleanWrapper(var booleanValue: Boolean) {
    fun setValue(newValue: Boolean) {
        booleanValue = newValue
    }
}
