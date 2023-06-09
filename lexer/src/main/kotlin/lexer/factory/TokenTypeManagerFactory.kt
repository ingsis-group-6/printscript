package lexer.factory

import TokenTypeManager
import common.token.TokenType
import lexer.implementation.TokenTypeChecker

class TokenTypeManagerFactory {

    companion object {
        fun createPrintScriptTokenTypeManager(): TokenTypeManager {
            val keywords = listOf("let", "number", "string", "println")
            return TokenTypeManager(generateCheckerList(keywords))
        }
        private fun generateCheckerList(keywords: List<String>): List<TokenTypeChecker> {
            return listOf(
                TokenTypeChecker(TokenType.ASSIGNATION) { string -> string == "=" },
                TokenTypeChecker(TokenType.SEMICOLON) { string -> string == ";" },
                TokenTypeChecker(TokenType.COLON) { string -> string == ":" },
                TokenTypeChecker(TokenType.WHITESPACE) { string -> listOf(" ", "\n", "\t").contains(string) },

                TokenTypeChecker(TokenType.IDENTIFIER) { string -> string[0].isLetter() && !keywords.contains(string) },

                TokenTypeChecker(TokenType.LET) { string -> string == "let" },
                TokenTypeChecker(TokenType.PRINTLN) { string -> string == "println" },

//                TokenTypeChecker(TokenType.NUMBER_TYPE) { string -> string == "number" },
//                TokenTypeChecker(TokenType.STRING_TYPE) { string -> string == "string" },
                TokenTypeChecker(TokenType.TYPE) { string -> string == "number" || string == "string" },

//                TokenTypeChecker(TokenType.PLUS) { string -> string == "+" },
//                TokenTypeChecker(TokenType.MINUS) { string -> string == "-" },
//                TokenTypeChecker(TokenType.TIMES) { string -> string == "*" },
//                TokenTypeChecker(TokenType.DIVIDED_BY) { string -> string == "/" },
                TokenTypeChecker(TokenType.OPEN_PARENTHESIS) { string -> string == "(" },
                TokenTypeChecker(TokenType.CLOSE_PARENTHESIS) { string -> string == ")" },

                TokenTypeChecker(TokenType.OPERATOR) { string -> listOf("+", "-", "*", "/").contains(string) },

                TokenTypeChecker(TokenType.NUMERIC_LITERAL) { string -> string[0].isDigit() },
                TokenTypeChecker(TokenType.STRING_LITERAL) {
                        string ->
                    (string.startsWith('"') && string.endsWith('"')) || (string.startsWith('\'') && string.endsWith('\''))
                },
                TokenTypeChecker(TokenType.EQUALS) { string -> string == "==" },
                TokenTypeChecker(TokenType.GREATER_EQUALS) { string -> string == ">=" },
                TokenTypeChecker(TokenType.LESSER_EQUALS) { string -> string == "<=" },
                TokenTypeChecker(TokenType.GREATER_THAN) { string -> string == ">" },
                TokenTypeChecker(TokenType.LESSER_THAN) { string -> string == "<" },
                TokenTypeChecker(TokenType.DIFFERENT) { string -> string == "!=" },
                TokenTypeChecker(TokenType.AND) { string -> string == "&&" },
                TokenTypeChecker(TokenType.OR) { string -> string == "||" }
            )
        }
    }
}
// string.matches(Regex("^\'[a-zA-Z_$].+( [a-zA-Z_\$])*\'"))
