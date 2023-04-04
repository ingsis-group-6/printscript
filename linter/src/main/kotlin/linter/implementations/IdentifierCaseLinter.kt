package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.DeclarationAST
import linter.`interface`.Linter

class IdentifierCaseLinter(private val caseToCheck: CaseConvention) : Linter {
    val caseChecker = CaseChecker()

    override fun lint(ast: AST) {
        if (ast is DeclarationAST) {
            if (caseChecker.checkCase(caseToCheck, ast.getIdentifier())) {
                val currentLine = ast.getTokensInLine().first().row
                println("(Line $currentLine) - There is an expression passed as argument for function.")
            }
        }
    }
}

class CaseChecker {

    fun checkCase(case: CaseConvention, stringToCheck: String): Boolean =
        when (case) {
            CaseConvention.CAMEL_CASE -> isCamelCase(stringToCheck)
            CaseConvention.SNAKE_CASE -> isSnakeCase(stringToCheck)
        }

    private fun isCamelCase(s: String): Boolean = s.isNotEmpty() && s.first().isLowerCase() && s.all { it.isLetter() || it.isDigit() }

    private fun isSnakeCase(s: String): Boolean = s.isNotEmpty() && s.all { (it.isLetter() && it.isLowerCase()) || it == '_' || it.isDigit() }
}

enum class CaseConvention {
    CAMEL_CASE,
    SNAKE_CASE
}
