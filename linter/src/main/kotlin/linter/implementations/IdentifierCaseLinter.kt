package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import linter.`interface`.Linter

class IdentifierCaseLinter(private val caseToCheck: CaseConvention) : Linter {
    val caseChecker = CaseChecker()

    override fun lint(ast: AST) {
        lintCompoundAST(ast)
        if (ast is DeclarationAST) {
            if (!caseChecker.isValidCase(caseToCheck, ast.getIdentifier())) {
                val currentLine = ast.getTokensInLine().first().row
                println("(Line $currentLine) - Naming convention violated. Identifier should be ${caseToCheck.displayName}.")
            }
        }
    }

    private fun lintCompoundAST(ast: AST) {
        if (ast is DeclarationAssignationAST) {
            lint(ast.getDeclarationAST())
            lint(ast.getAssignationAST())
        }
    }
}

class CaseChecker {

    fun isValidCase(case: CaseConvention, stringToCheck: String): Boolean =
        when (case) {
            CaseConvention.CAMEL_CASE -> isCamelCase(stringToCheck)
            CaseConvention.SNAKE_CASE -> isSnakeCase(stringToCheck)
        }

    private fun isCamelCase(s: String): Boolean = s.isNotEmpty() && s.first().isLowerCase() && s.all { it.isLetter() || it.isDigit() }

    private fun isSnakeCase(s: String): Boolean = s.isNotEmpty() && s.all { (it.isLetter() && it.isLowerCase()) || it == '_' || it.isDigit() }
}

enum class CaseConvention(val displayName: String) {
    CAMEL_CASE("Camel Case"),
    SNAKE_CASE("Snake Case")
}
