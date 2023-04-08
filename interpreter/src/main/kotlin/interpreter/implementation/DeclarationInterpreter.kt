package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.DeclarationAST
import interpreter.interfaces.Interpreter

class DeclarationInterpreter(
    private val symbolTable: MutableMap<String, Pair<String, String?>>
) : Interpreter {

    override fun interpret(ast: AST) {
        ast as DeclarationAST
        val identifier = ast.getIdentifier()
        val currentLine = ast.getTokensInLine().first().row
        if (identifier in symbolTable.keys) throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is already declared")
        val type = ast.getType()
        symbolTable[identifier] = Pair(type, null)
    }
}
