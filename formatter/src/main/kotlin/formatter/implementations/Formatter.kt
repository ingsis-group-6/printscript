package formatter.implementations

import common.ast.AST
import common.ast.implementations.asts.*
import common.config.reader.formatter.FormatterRules
import common.token.Token
import common.token.TokenType
import formatter.`interface`.Formatter

class Formatter(configFileName: String) : Formatter {
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
            is ConditionalAST -> {
                formatConditionalAST(ast, tokensInLine)
            }
            else -> { "" }
        }
    }

    private fun formatConditionalAST(ast: ConditionalAST, tokensInLine: List<Token>): String {
        val formattedIfASTs = ast
            .getIfBlock()
            .getContainedASTs()
            .map { containedAST -> createWhitespaceString(formatterRules.custom.conditionalBlockIndentation) + format(containedAST) }
            .joinToString("")
        val formattedElseASTs = ast
            .getElseBlock()
            .getContainedASTs()
            .map { containedAST -> format(containedAST) }
            .joinToString("")

        val noElseCase = "if(${ast.getCondition().getValue()}) {" +
            "\n" + formattedIfASTs + "}"

        val elseClause = " else {\n" + formattedElseASTs + "}"

        return if (formattedElseASTs.isEmpty()) noElseCase else noElseCase + elseClause
    }

    private fun formatEOFAST(tokensInLine: List<Token>): String {
        return "EOF"
    }

    fun createWhitespaceString(n: Int): String {
        return " ".repeat(n)
    }

    // PRINTLN ( [EXPR | LITERAL | ID] ) ;
    fun formatFunctionAST(tokens: List<Token>): String {
        return if (tokens.first().tokenType == TokenType.PRINTLN) {
            "println(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");\n"
        } else {
            "${tokens.first().value}(" + formatExpression(tokens.subList(2, tokens.lastIndex - 1)) + ");\n"
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
            tokens.last().value +
            "\n"

    //
    fun formatDeclarationAST(tokens: List<Token>): String =
        tokens[0].value +
            createWhitespaceString(formatterRules.spacesBetweenTokens) +
            tokens[1].value +
            createWhitespaceString(formatterRules.custom.spaceBeforeColon) +
            tokens[2].value +
            createWhitespaceString(formatterRules.custom.spaceAfterColon) +
            tokens[3].value +
            tokens[4].value +
            "\n"

    fun formatDeclarationAssignationAST(tokens: List<Token>): String {
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
