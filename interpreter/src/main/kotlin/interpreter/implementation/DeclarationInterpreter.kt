package interpreter.implementation

import common.ast.implementations.asts.AST
import common.ast.implementations.asts.DeclarationAST
import interpreter.interfaces.Interpreter
import interpreter.interfaces.Scope

class DeclarationInterpreter(
    private val scope: Scope
) : Interpreter {

    override fun interpret(ast: AST) {
        ast as DeclarationAST
        val identifier = ast.getIdentifier()
        val declarator = ast.getDeclarator()
        val currentLine = ast.getTokensInLine().first().row
        // if (identifier in mutableSymbolTable.keys || identifier in immutableSymbolTable.keys) throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is already declared")
        if (scope.existsVariable(identifier)) throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is already declared")
        val type = ast.getType()
        if (declarator == "let") {
            scope.putMutableVariable(identifier, Pair(type, null), currentLine)
        } else {
            scope.putImmutableVariable(identifier, Pair(type, null), currentLine)
        }
        // mutableSymbolTable[identifier] = Pair(type, null) else
        // immutableSymbolTable[identifier] = Pair(type, null)
    }
}
