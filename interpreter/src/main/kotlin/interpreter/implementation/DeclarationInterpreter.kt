package interpreter.implementation

import common.ast.AST
import common.ast.implementations.asts.DeclarationAST
import interpreter.interfaces.Interpreter

class DeclarationInterpreter(
    private val mutableSymbolTable: MutableMap<String, Pair<String, String?>>,
    private val immutableSymbolTable: MutableMap<String, Pair<String, String?>>
) : Interpreter {

    override fun interpret(ast: AST) {
        ast as DeclarationAST
        val identifier = ast.getIdentifier()
        val declarator = ast.getDeclarator()
        val currentLine = ast.getTokensInLine().first().row
        if (identifier in mutableSymbolTable.keys || identifier in immutableSymbolTable.keys) throw Exception("(Line $currentLine) - Variable ${ast.getIdentifier()} is already declared")
        val type = ast.getType()
        if (declarator == "let") mutableSymbolTable[identifier] = Pair(type, null) else immutableSymbolTable[identifier] = Pair(type, null)
    }
}
