package parser.implementation

import common.ast.AST
import common.ast.ASTFactory
import common.ast.ASTType
import common.ast.implementations.asts.EmptyAST
import common.exceptions.InvalidTokenInputException
import common.token.Token
import common.token.TokenType
import parser.exceptions.EmptyTokenInputException
import parser.interfaces.Parser

class Parser : Parser {

    fun check(tokens: List<Token>): Pair<AST, List<String>> {
        val errorList = mutableListOf<String>()
        try {
            val ast = parse(tokens)
            return Pair(ast, errorList)
        } catch (exc: InvalidTokenInputException) {
            errorList.add(exc.message!!)
            return Pair(EmptyAST, errorList)
        }
    }

    // let MyNumber: number = ;

    override fun parse(tokens: List<Token>): AST {
        if (tokens.isEmpty()) throw EmptyTokenInputException("The input token list is empty.")
        return generateTreeFromTokenList(clearWhitespaces(tokens))
    }

    private fun clearWhitespaces(tokens: List<Token>) =
        tokens.filter { token: Token -> token.tokenType != TokenType.WHITESPACE }

    private fun generateTreeFromTokenList(tokens: List<Token>): AST {
        val inputTokenTypes = tokens.map { it.tokenType }

        val foundAST = detectASTType(inputTokenTypes, tokens.first().row)
        return ASTFactory.createAST(foundAST, tokens)
    }

    fun detectASTType(inputTokenTypes: List<TokenType>, row: Int): ASTType {
        val foundAST = when {
            inputTokenTypes.first() == TokenType.IDENTIFIER -> ASTType.ASSIGNATION
            inputTokenTypes.contains(TokenType.LET) && !inputTokenTypes.contains(TokenType.ASSIGNATION) -> ASTType.DECLARATION
            inputTokenTypes.contains(TokenType.ASSIGNATION) -> ASTType.DECLARATION_ASSIGNATION
            inputTokenTypes.first() == TokenType.PRINTLN || inputTokenTypes.first() == TokenType.FUNCTION -> ASTType.FUNCTION
            else -> throw Exception("(Line $row) - Malformed Expression. AST not detected.")
        }
        return foundAST
    }
}
