package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.AssignationAST
import common.ast.implementations.asts.DeclarationAST
import common.ast.implementations.asts.DeclarationAssignationAST
import common.config.reader.linter.CaseConvention
import linter.`interface`.Linter

class IdentifierCaseLinter(private val caseToCheck: CaseConvention) : Linter {
    private val caseChecker = CaseChecker()

    override fun lint(ast: Pair<AST, List<String>>) {
        lintCompoundAST(ast)
        if (ast.first is DeclarationAST) {
            if (!caseChecker.isValidCase(caseToCheck, (ast.first as DeclarationAST).getIdentifier())) {
                val currentLine = ast.first.getTokensInLine().first().row
                println("(Line $currentLine) - Naming convention violated. Identifier should be ${caseToCheck.displayName}.")
            }
        }
    }

    private fun lintCompoundAST(ast: Pair<AST, List<String>>) {
        if (ast.first is DeclarationAssignationAST) {
            lint(Pair((ast.first as DeclarationAST), ast.second))
            lint(Pair((ast.first as AssignationAST), ast.second))
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
