package common.ast

import common.ast.implementations.asts.*
import common.ast.implementations.asts.AssignationAST
import common.token.Token

object ASTFactory {

    fun createAST(type: ASTType, tokens: List<Token>): AST {
        return when (type) {
            ASTType.ASSIGNATION -> AssignationAST(tokens) // aca va la logica de crear el assignation
            ASTType.DECLARATION -> DeclarationAST(tokens)
            ASTType.DECLARATION_ASSIGNATION -> DeclarationAssignationAST(tokens)
            ASTType.FUNCTION -> FunctionAST(tokens)
            ASTType.EOF -> EndOfFileAST
        }
    }
}
