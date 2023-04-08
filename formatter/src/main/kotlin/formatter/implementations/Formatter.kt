package formatter.implementations

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.ast.implementations.asts.FunctionAST
import common.config.reader.formatter.FormatterRules
import common.token.Token
import common.token.TokenType
import formatter.`interface`.Formatter

class Formatter(configFileName: String) : Formatter {
    private val formatterRules = FormatterRules(configFileName)

    override fun format(ast: AST) {
        val tokensInLine = ast.getTokensInLine()
        when (ast) {
            is FunctionAST -> {
                formatFunctionAST(tokensInLine)
            }
            is DeclarationAST -> {
                formatDeclarationAST(tokensInLine)
            }
            is AssignationAST -> {
                formatAssignationAST(tokensInLine)
            }
            is DeclarationAssignationAST -> {
                formatDeclarationAssignationAST(tokensInLine)
            }
            else -> {}
        }
    }

    fun createWhitespaceString(n: Int): String {
        return " ".repeat(n)
    }

    // PRINTLN ( [EXPR | LITERAL | ID] ) ;
    fun formatFunctionAST(tokens: List<Token>): String {
        return if (tokens.first().tokenType == TokenType.PRINTLN) {
            "println(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");"
        } else {
            "${tokens.first().value}(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");"
        }
    }
    fun formatExpression(tokens: List<Token>): String {
        if (tokens.isEmpty()) return ""
        if (tokens.size == 1) return tokens.first().value
        return tokens.map { it.value }.joinToString(separator = createWhitespaceString(formatterRules.spacesBetweenTokens))
    } // 4, /, 2 -> "4 / 2";

    // [ID] [=] [LITERAL | EXPRESSION | ID] [SEMICOLON]
    fun formatAssignationAST(tokens: List<Token>): String =
        tokens[0].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            tokens[1].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            formatExpression(tokens.subList(2, tokens.lastIndex)) +
            tokens.last().value

    //
    fun formatDeclarationAST(tokens: List<Token>) =
        tokens[0].value +
            createWhitespaceString(formatterRules.spacesBetweenTokens) +
            tokens[1].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeColon) +
            tokens[2].value +
            createWhitespaceString(formatterRules.custom.spaceAfterColon) +
            tokens[3].value +
            tokens[4].value

    fun formatDeclarationAssignationAST(tokens: List<Token>): String {
        val declarationString = formatDeclarationAST(tokens.subList(0, 5))
        val expressionString = formatExpression(tokens.subList(5, tokens.lastIndex))

        return declarationString.substring(0, declarationString.length - 1) +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            "=" +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            expressionString +
            tokens.last().value
    }
}
