package interpreter.implementation

import common.ast.implementations.asts.DeclarationAST
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope

class DeclarationInterpreter(
    private val scope: Scope
) : Interpreter<DeclarationAST> {

    override fun interpret(ast: DeclarationAST) {
        val identifier = ast.getIdentifier()
        val declarator = ast.getDeclarator()
        val currentLine = ast.getTokensInLine().first().row
        if (scope.existsVariable(identifier)) throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is already declared")
        val type = ast.getType()
        if (declarator == "let") {
            scope.putMutableVariable(identifier, Pair(type, null), currentLine)
        } else {
            scope.putImmutableVariable(identifier, Pair(type, null), currentLine)
        }
    }
}
