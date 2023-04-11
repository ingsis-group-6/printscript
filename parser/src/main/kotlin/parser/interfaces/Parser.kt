package parser.interfaces

import common.ast.AST
import common.token.Token

interface Parser {

    fun parse(tokens: List<Token>): AST
    fun check(tokens: List<Token>): Pair<AST, List<String>>
}
