package lexer.implementation

import common.token.Token
import common.token.TokenType
import lexer.factory.TokenTypeManagerFactory
import lexer.`interface`.Lexer
import lexer.`interface`.TokenTypeManager
import lexer.util.StringReadingChecker
import java.io.File
import java.util.*

class Lexer(
    private val list: TokenTypeManager,
    private val tokenChars: List<Char>
) : Lexer { // chars que separan las palabras, pero representan una operacion y se incluyen como token

    constructor() : this(TokenTypeManagerFactory.createPrintScriptTokenTypeManager("1.0"), listOf(';', ':', '(', ')', ' ', '\n', '\t', '+', '=', '-', '*', '/')) {}

    override fun extractTokensFromFile(file: File) {
        val scanner = Scanner(file)
        var currentLineNumber = 1
        val fileToWrite = File("Tokens.txt")
        fileToWrite.writeText("")
        while (scanner.hasNextLine()) {
            val current = scanner.nextLine()
            if (current.isEmpty()) {
                currentLineNumber++
                continue
            }
            val lineTokenList = extractTokensFromLine(current, currentLineNumber)
            currentLineNumber++
            fileToWrite.appendText(lineTokenList.joinToString("\n") { it.toString() })
            fileToWrite.appendText("\n")
        }
    }

    fun extractTokensFromLine(inputLine: String, row: Int): List<Token> {
        val stringWords = getWordsFromLine(inputLine)
        val tokenList: MutableList<Token> = mutableListOf()
        val tokenGeneratorFunction: (Int, TokenType, String, Int, Int) -> Token = { orderId, tokenType, value, row, col -> Token(orderId, tokenType, value, row, col) }
        var currentOrderId = 0
        var currentCol = 0

        for (word in stringWords) {
            for (tokenTypeChecker in list.getTokenTypeList()) {
                if (tokenTypeChecker.validateType(word)) {
                    tokenList.add(tokenGeneratorFunction(currentOrderId, tokenTypeChecker.tokenType, word, row, currentCol))
                    currentOrderId++
                }
            }
            currentCol += word.length
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
