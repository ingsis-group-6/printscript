package parser.interfaces

import common.ast.AST
import common.token.Token

interface Parser {

    fun parse(tokens: List<Token>): AST
    
}
