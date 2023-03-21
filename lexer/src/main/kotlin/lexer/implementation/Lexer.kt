package lexer.implementation

import lexer.`interface`.Lexer
import lexer.`interface`.TokenTypeManager
import lexer.token.Token
import lexer.token.TokenType
import java.io.File
import java.util.*


class Lexer(
    private val list: TokenTypeManager,
    private val wordSeparationChars: List<Char>, // chars que separan las palabras, ej: espacio, comas en los params de un metodo
    private val tokenChars: List<Char>) : Lexer { // chars que separan las palabras, pero representan una operacion y se incluyen como token


    override fun extractTokensFromFile(file: File): List<Token> {
        val listOfTokens = mutableListOf<Token>()
        val scanner = Scanner(file)

        var currentLine = 0
        while(scanner.hasNextLine()){
            val lineTokenList = extractTokensFromLine(scanner.nextLine(), currentLine)
            currentLine++
            listOfTokens.addAll(lineTokenList)
        }
        return listOfTokens
    }


    fun extractTokensFromLine(inputLine: String, row: Int): List<Token> {
        val stringWords = getWordsFromLine(inputLine)
        val tokenList: MutableList<Token> = mutableListOf()
        val tokenGeneratorFunction: (Int, TokenType, String, Int) -> Token = { orderId, tokenType, value, row -> Token(orderId, tokenType, value, row)}
        var currentOrderId = 0

        for (word in stringWords){
            for (tokenTypeChecker in list.getTokenTypeList()){
                if(tokenTypeChecker.validateType(word)){
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

            if(StringReadingChecker.isPartOfString(char)) {
                soFar += char
                continue
            }
            if (!wordSeparationChars.contains(char) && !tokenChars.contains(char)) {
                if (tokenChars.contains(char)) {
                    stringsToReturn.add(char.toString())
                    soFar = ""
                }
                soFar += char
            } else {
                if (soFar != "") stringsToReturn.add(soFar)
                if (tokenChars.contains(char)) stringsToReturn.add(char.toString())
                soFar = ""
            }
        }
        if(soFar != "") stringsToReturn.add(soFar)
        return stringsToReturn;
    }


}

object StringReadingChecker {
    private const val EMPTY_CHAR = '\u0000'
    private var previousChar = EMPTY_CHAR // k
    private var currentStringClosingChar = EMPTY_CHAR // "


    fun isPartOfString(currentChar: Char): Boolean{
        // cuando no hay string
        if((currentChar != '"' && currentChar != '\'') && currentStringClosingChar == EMPTY_CHAR){
            return false
        }

        // cuando arranca el str
        if((currentChar == '"' || currentChar == '\'') && currentStringClosingChar == EMPTY_CHAR) {
            currentStringClosingChar = currentChar
            return true
        }

        // cuando estas dentro del str
        if (currentChar != currentStringClosingChar && currentStringClosingChar != EMPTY_CHAR) {
            previousChar = currentChar
            return true
        }

        // cuando cierra el str
        if(currentChar == currentStringClosingChar && previousChar != '\\' && currentStringClosingChar != EMPTY_CHAR) {
            currentStringClosingChar = EMPTY_CHAR
            previousChar = EMPTY_CHAR
            return true
        }
        return false
    }

}

