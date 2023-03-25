package parser

import common.token.TokenType

object TokenTypeListGenerator {



    fun generateTokenTypes(vararg tokensStrings: String): List<TokenType> {
        val listToReturn = mutableListOf<TokenType>()
        for (tokenString in tokensStrings) {
            listToReturn.add(TokenType.valueOf(tokenString))
        }
        return listToReturn

    }



}