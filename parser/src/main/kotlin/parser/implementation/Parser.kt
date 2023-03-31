package parser.implementation

import common.ast.AST
import common.ast.ASTFactory
import common.ast.ASTType
import common.token.Token
import common.token.TokenType
import parser.interfaces.Parser

class Parser : Parser {

    override fun parse(tokens: List<Token>): AST {
        return generateTreeFromTokenList(tokens)
    }

    private fun generateTreeFromTokenList(tokens: List<Token>): AST {
        val inputTokenTypes = tokens.map { it.tokenType }

        val foundAST = detectASTType(inputTokenTypes)
        return ASTFactory.createAST(foundAST, tokens)
    }


    fun detectASTType(inputTokenTypes: List<TokenType>): ASTType {
        val foundAST = when {
            inputTokenTypes.first() == TokenType.IDENTIFIER -> ASTType.ASSIGNATION
            inputTokenTypes.contains(TokenType.LET) && !inputTokenTypes.contains(TokenType.ASSIGNATION) -> ASTType.DECLARATION
            inputTokenTypes.contains(TokenType.ASSIGNATION) -> ASTType.DECLARATION_ASSIGNATION
            else -> ASTType.FUNCTION
        }
        return foundAST
    }
}
