package formatter.implementations

import common.ast.implementations.asts.*
import common.config.reader.formatter.FormatterRules
import common.token.Token
import common.token.TokenType
import formatter.`interface`.Formatter

class Formatter<T>(configFileName: String) : Formatter<AST> {
    private val formatterRules = FormatterRules(configFileName)

    override fun format(ast: AST): String {
        val tokensInLine = ast.getTokensInLine()

        return when (ast) {
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
            is EndOfFileAST -> {
                formatEOFAST(tokensInLine)
            }

            is BlockAST -> TODO()
            is ConditionalAST -> TODO()
            EmptyAST -> TODO()
        }
    }

    private fun formatEOFAST(tokensInLine: List<Token>): String {
        return "EOF"
    }

    fun createWhitespaceString(n: Int): String {
        return " ".repeat(n)
    }

    // PRINTLN ( [EXPR | LITERAL | ID] ) ;
    private fun formatFunctionAST(tokens: List<Token>): String {
        return if (tokens.first().tokenType == TokenType.PRINTLN) {
            "println(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");\n"
        } else {
            "${tokens.first().value}(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");\n"
        }
    }
    private fun formatExpression(tokens: List<Token>): String {
        if (tokens.isEmpty()) return ""
        if (tokens.size == 1) return tokens.first().value
        return tokens.map { it.value }.joinToString(separator = createWhitespaceString(formatterRules.spacesBetweenTokens))
    } // 4, /, 2 -> "4 / 2";

    // [ID] [=] [LITERAL | EXPRESSION | ID] [SEMICOLON]
    private fun formatAssignationAST(tokens: List<Token>): String =
        tokens[0].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            tokens[1].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            formatExpression(tokens.subList(2, tokens.lastIndex)) +
            tokens.last().value +
            "\n"

    //
    private fun formatDeclarationAST(tokens: List<Token>): String =
        tokens[0].value +
            createWhitespaceString(formatterRules.spacesBetweenTokens) +
            tokens[1].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeColon) +
            tokens[2].value +
            createWhitespaceString(formatterRules.custom.spaceAfterColon) +
            tokens[3].value +
            tokens[4].value +
            "\n"

    private fun formatDeclarationAssignationAST(tokens: List<Token>): String {
        val declarationString = formatDeclarationAST(tokens.subList(0, 5))
        val expressionString = formatExpression(tokens.subList(5, tokens.lastIndex))

        return declarationString.substring(0, declarationString.length - 2) +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            "=" +
            createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
            expressionString +
            tokens.last().value +
            "\n"
    }
}
