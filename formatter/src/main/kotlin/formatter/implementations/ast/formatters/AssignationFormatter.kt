package formatter.implementations.ast.formatters
import common.ast.implementations.asts.AssignationAST
import common.config.reader.formatter.FormatterRules
import common.token.Token
import formatter.`interface`.Formatter

class AssignationFormatter(private val formatterRules: FormatterRules) :  Formatter<AssignationAST>{

    override fun format(ast: AssignationAST): String {
        val tokens = ast.getTokensInLine()
        return tokens[0].value +
                createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
                tokens[1].value +
                createWhitespaceString(formatterRules.custom.spaceBeforeAndAfterAssignationOperator) +
                formatExpression(tokens.subList(2, tokens.lastIndex)) +
                tokens.last().value +
                "\n"
    }

    private fun createWhitespaceString(n: Int): String {
        return " ".repeat(n)
    }

    private fun formatExpression(tokens: List<Token>): String {
        if (tokens.isEmpty()) return ""
        if (tokens.size == 1) return tokens.first().value
        return tokens.map { it.value }.joinToString(separator = createWhitespaceString(formatterRules.spacesBetweenTokens))
    }


}