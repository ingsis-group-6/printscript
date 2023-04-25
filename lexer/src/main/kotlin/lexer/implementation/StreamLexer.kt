package lexer.implementation

import common.token.Token
import common.token.TokenType
import lexer.`interface`.TokenTypeManager
import lexer.util.StringReadingChecker
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.util.Optional
import java.util.PriorityQueue

class StreamLexer(
    stream: InputStream,
    private val list: TokenTypeManager,
    private val tokenChars: List<Char>
) {

    private val reader: Reader = InputStreamReader(stream)
    private var soFar: String = ""
    private val tokenGeneratorFunction: (Int, TokenType, String, Int, Int) -> Token =
        { orderId, tokenType, value, row, col -> Token(orderId, tokenType, value, row, col) }
    private val tokenTypeCheckers = list.getTokenTypeList()

    private var currentOrderId = 0
    private var currentRow = 1
    private var pendingTokens = PriorityQueue<Optional<Token>>(
        Comparator {
                token1, token2 ->
            token1.get().order_id.compareTo(token2.get().order_id)
        }
    )

    fun lexToken(): Optional<Token> {
        if (pendingTokens.isNotEmpty()) return pendingTokens.poll()

        val currentCharAsInt = reader.read()
        if (currentCharAsInt == -1) return Optional.of(Token(currentOrderId, TokenType.EOF, "", currentRow, 0))

        val currentChar = currentCharAsInt.toChar()
        if (currentChar == '\n') {
            currentRow++
        }

        if (StringReadingChecker.isPartOfString(currentChar)) {
            soFar += currentChar
            return Optional.empty()
        }
        if (isASeparationToken(currentChar)) {
            soFar += currentChar
            return Optional.empty()
        } else { // en este caso, rompe el orderId
            val currentCharOptional = checkIfToken(currentChar.toString())
            val toReturn: Optional<Token> = if (soFar != "") {
                pendingTokens.offer(currentCharOptional)
                val soFarToken = checkIfToken(soFar)

                val breakCharTokenOrderId = currentCharOptional.get().order_id
                currentCharOptional.get().order_id = soFarToken.get().order_id
                soFarToken.get().order_id = breakCharTokenOrderId

                soFarToken
            } else {
                currentCharOptional
            }
            soFar = ""
            return toReturn
        }
    }

    private fun isASeparationToken(currentChar: Char) = !tokenChars.contains(currentChar)

    private fun checkIfToken(soFar: String): Optional<Token> {
        for (tokenTypeChecker in tokenTypeCheckers) {
            if (tokenTypeChecker.validateType(soFar)) {
                currentOrderId++
                return Optional.of(tokenGeneratorFunction(currentOrderId, tokenTypeChecker.tokenType, soFar, currentRow, 0))
            }
        }
        return Optional.empty<Token>()
    }

    fun peekToken(): Token {
        if (pendingTokens.isNotEmpty() && pendingTokens.peek().get().tokenType != TokenType.WHITESPACE) return pendingTokens.peek().get()
        var nextToken = lexToken()
        while (nextToken.isEmpty || nextToken.get().tokenType == TokenType.WHITESPACE) {
            nextToken = lexToken()
        }
        val tokenToReturn = nextToken.get().copy()
        pendingTokens.offer(nextToken)
//        forceFirstPosition(nextToken)

        return tokenToReturn
    }

//    private fun forceFirstPosition(token: Optional<Token>) {
//        val auxQueue: Queue<Optional<Token>> = LinkedList()
//        while(pendingTokens.isNotEmpty()){
//            auxQueue.offer(pendingTokens.poll())
//        }
//        pendingTokens.offer(token)
//        while(auxQueue.isNotEmpty()){
//            pendingTokens.offer(auxQueue.poll())
//        }
//    }
}
