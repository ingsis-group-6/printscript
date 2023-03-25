package common.ast

import common.ast.implementations.AssignationAST
import common.ast.implementations.DeclarationAST
import common.ast.implementations.DeclarationAssignationAST
import common.ast.implementations.FunctionAST
import common.token.Token

object ASTFactory {

    fun createAST(type: ASTType, tokens: List<Token>): AST {
        return when(type) {
            ASTType.ASSIGNATION -> AssignationAST() // aca va la logica de crear el assignation
            ASTType.DECLARATION -> DeclarationAST(tokens)
            ASTType.DECLARATION_ASSIGNATION -> DeclarationAssignationAST()
            ASTType.FUNCTION -> FunctionAST()
        }
    }

}