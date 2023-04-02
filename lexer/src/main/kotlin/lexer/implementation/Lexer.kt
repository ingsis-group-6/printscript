package lexer.implementation

import common.token.Token
import common.token.TokenType
import lexer.`interface`.Lexer
import lexer.`interface`.TokenTypeManager
import lexer.util.StringReadingChecker
import java.io.File
import java.util.*

class Lexer(
    private val list: TokenTypeManager,
    private val tokenChars: List<Char>
) : Lexer { // chars que separan las palabras, pero representan una operacion y se incluyen como token

    override fun extractTokensFromFile(file: File) {
        val scanner = Scanner(file)
        var currentLine = 0
        val fileToWrite = File("Tokens.txt")
        fileToWrite.writeText("")
        println(Token(1, TokenType.TYPE, "dummy", 0).toString())
        while (scanner.hasNextLine()) {
            val lineTokenList = extractTokensFromLine(scanner.nextLine(), currentLine)
            currentLine++
            fileToWrite.appendText(lineTokenList.joinToString("\n") { it.toString() })
            fileToWrite.appendText("\n")
        }
    }

    fun extractTokensFromLine(inputLine: String, row: Int): List<Token> {
        val stringWords = getWordsFromLine(inputLine)
        val tokenList: MutableList<Token> = mutableListOf()
        val tokenGeneratorFunction: (Int, TokenType, String, Int) -> Token = { orderId, tokenType, value, row -> Token(orderId, tokenType, value, row) }
        var currentOrderId = 0

        for (word in stringWords) {
            for (tokenTypeChecker in list.getTokenTypeList()) {
                if (tokenTypeChecker.validateType(word)) {
                    tokenList.add(tokenGeneratorFunction(currentOrderId, tokenTypeChecker.tokenType, word, row))
                    currentOrderId++
                }
            }
        }
        return tokenList
    }

    fun getWordsFromLine(line: String): List<String> {
        val stringsToReturn: MutableList<String> = mutableListOf()
        var soFar = ""

        for (char in line) {
            if (StringReadingChecker.isPartOfString(char)) {
                soFar += char
                continue
            }
            if (!tokenChars.contains(char)) {
                soFar += char
            } else {
                if (soFar != "") stringsToReturn.add(soFar)
                stringsToReturn.add(char.toString())
                soFar = ""
            }
        }
        if (soFar != "") stringsToReturn.add(soFar)
        return stringsToReturn
    }
}
