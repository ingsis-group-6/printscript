package parser.provider

import common.ast.AST
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

    override fun checkASTCreation(): Optional<Pair<AST, List<String>>> {
        val tokenProviderResult = tokenProvider.getToken()
        if (tokenProviderResult.isEmpty) return Optional.empty()

        tokensReceivedSoFar.add(tokenProviderResult.get())
        if (tokensReceivedSoFar.last().tokenType == TokenType.SEMICOLON) {
            val astCheckResult = parser.check(tokensReceivedSoFar)
            tokensReceivedSoFar.clear()
            return Optional.of(astCheckResult)
        } else {
            return Optional.empty()
        }
    }
}
