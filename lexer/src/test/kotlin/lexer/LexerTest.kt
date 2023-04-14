package lexer

import lexer.factory.TokenTypeManagerFactory
import lexer.implementation.Lexer
import org.junit.jupiter.api.Test
import java.io.File

class LexerTest {

    private val lexer =
        Lexer(TokenTypeManagerFactory.createPrintScriptTokenTypeManager(), listOf(';', ':', '(', ')', ' ', '\n', '\t', '+', '=', '-', '*', '/'))

    private fun countLinesInFile(file: File): Int {
        var lineCount = 0
        file.forEachLine { lineCount++ }
        return lineCount
    }

    @Test
    fun testOneLineFile2() {
        lexer.extractTokensFromFile(File("src/test/resources/OneLineFile2.txt"))
        assert(countLinesInFile(File("Tokens.txt")) == 16)
    }

    @Test
    fun testFiveLineFile() {
        lexer.extractTokensFromFile(File("src/test/resources/FiveLineFile.txt"))
        assert(countLinesInFile(File("Tokens.txt")) == 55)
    }

    @Test
    fun testEmptyLineFile() {
        lexer.extractTokensFromFile(File("src/test/resources/EmptyFile.txt"))
        assert(countLinesInFile(File("Tokens.txt")) == 0)
    }

    @Test
    fun testConsecutiveLine() {
        lexer.extractTokensFromFile(File("src/test/resources/ConsecutiveLineFile.txt"))
        assert(countLinesInFile(File("Tokens.txt")) == 15)
    }

    @Test
    fun testBooleanInput() {
        val input = "const myVar: boolean = true;"
//        val expectedList = listOf(
//            Token(1, TokenType.DECLARATOR, "let", 0, 1)
//        )

        ResultOutput.writeListToFile(lexer.extractTokensFromLine(input, 1), "src/test/resources/outputs/boolean1_output.txt")
    }
}
