package parser.provider

import common.ast.AST
import common.ast.implementations.asts.ConditionalAST
import common.providers.ast.ASTErrorReporter
import common.providers.ast.ASTProvider
import common.providers.token.TokenProvider
import common.token.Token
import common.token.TokenType
import parser.implementation.Parser
import java.util.*

class ASTProvider(private val tokenProvider: TokenProvider) : ASTProvider, ASTErrorReporter {
    private val tokensReceivedSoFar = mutableListOf<Token>()
    private val parser = Parser()

    override fun getAST(): Optional<AST> {
        val tokenProviderResult = tokenProvider.getToken()
        if (tokenProviderResult.isEmpty) return Optional.empty()

        if (tokenProviderResult.get().tokenType == TokenType.IF) {
            val ast = parseConditionalAST(tokenProviderResult)
            return ast
        }

        tokensReceivedSoFar.add(tokenProviderResult.get())
        if (tokensReceivedSoFar.last().tokenType == TokenType.SEMICOLON) {
            val ast = parser.parse(tokensReceivedSoFar)
            tokensReceivedSoFar.clear()
            return Optional.of(ast)
        } else if (tokenProviderResult.isPresent && tokenProviderResult.get().tokenType === TokenType.EOF) {
            return Optional.of(parser.parse(listOf(tokenProviderResult.get())))
        } else {
            return Optional.empty()
        }
    }

    private fun parseConditionalAST(tokenProviderResult: Optional<Token>): Optional<AST> {
        var elseBlockASTs = listOf<AST>()
        val ifToken = tokenProviderResult.get()
        val openParenthesis = getNextNonEmptyNonWhitespace()
        val condition = getNextNonEmptyNonWhitespace()
        val closedParenthesis = getNextNonEmptyNonWhitespace()
        val openingBrackets = getNextNonEmptyNonWhitespace()
        val ifBlockASTs: List<AST> = getBlock(tokenProvider.getToken(), listOf(), listOf())
        val nextNonTrivialToken = tokenProvider.peekToken()
        if (nextNonTrivialToken.tokenType == TokenType.ELSE) {
            val elseToken = getNextNonEmptyNonWhitespace()
            val openingElseBrackets = getNextNonEmptyNonWhitespace()
            elseBlockASTs = getBlock(tokenProvider.getToken(), listOf(), listOf())
        }
        val astToReturn = ConditionalAST(condition, ifBlockASTs, elseBlockASTs)
        return Optional.of(astToReturn)
    }

// IF ( [BOOL_TRUE | BOOL_FALSE | ID] ) {  ----ASTs--- }

    private fun getNextNonEmptyNonWhitespace(): Token {
        var currentToken = tokenProvider.getToken()
        while (currentToken.isEmpty || currentToken.get().tokenType == TokenType.WHITESPACE) {
            currentToken = tokenProvider.getToken()
        }
        return currentToken.get()
    }

    fun getBlock(token: Optional<Token>, tokensSoFar: List<Token>, astList: List<AST>): List<AST> {
        if (token.isPresent && token.get().tokenType == TokenType.CLOSE_CURLY_BRACKETS) {
            return astList
        }

        if (token.isEmpty || (token.isPresent && token.get().tokenType == TokenType.WHITESPACE)) {
            return getBlock(tokenProvider.getToken(), tokensSoFar, astList)
        }

        if (token.isPresent && token.get().tokenType == TokenType.SEMICOLON) {
            val ast = parser.parse(tokensSoFar + listOf(token.get()))
            return getBlock(tokenProvider.getToken(), listOf(), astList + listOf(ast))
        }

        if (token.isPresent && token.get().tokenType == TokenType.IF) {
            val ast = parseConditionalAST(token).get()
            return getBlock(tokenProvider.getToken(), listOf(), astList + listOf(ast))
        }

        return getBlock(tokenProvider.getToken(), tokensSoFar + listOf(token.get()), astList)
    }

    override fun checkASTCreation(): Optional<Pair<AST, List<String>>> {
        val tokenProviderResult = tokenProvider.getToken()
        if (tokenProviderResult.isEmpty) return Optional.empty()

        tokensReceivedSoFar.add(tokenProviderResult.get())
        if (tokensReceivedSoFar.last().tokenType == TokenType.SEMICOLON) {
            val astCheckResult = parser.check(tokensReceivedSoFar)
            tokensReceivedSoFar.clear()
            return Optional.of(astCheckResult)
        } else if (tokenProviderResult.isPresent && tokenProviderResult.get().tokenType === TokenType.EOF) {
            return Optional.of(parser.check(listOf(tokenProviderResult.get())))
        } else {
            return Optional.empty()
        }
    }
}
