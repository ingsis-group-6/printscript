package parser.interfaces

import common.ast.implementations.asts.AST
import common.token.Token

interface Parser {

    fun parse(tokens: List<Token>): AST
}
