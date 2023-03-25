package lexer

import lexer.factory.TokenTypeManagerFactory
import lexer.implementation.Lexer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class LexerTest {

    private val lexer = Lexer(TokenTypeManagerFactory.createPrintScriptTokenTypeManager(), listOf(';', ':', '(', ')', ' ', '\n', '\t'))

    @Test
    fun testOneLineFile1() {
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/OneLineFile1.txt"))
        tokens.map { println(it) }
        Assertions.assertEquals(4, tokens.size)
    }

    @Test
    fun testOneLineFile2() {
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/OneLineFile2.txt"))
        tokens.map { println(it) }
        ResultOutput.writeListToFile(tokens, "src/test/resources/outputs/testOneLineFile2_output.txt")
        Assertions.assertEquals(12, tokens.size)
    }

    @Test
    fun testFiveLineFile() {
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/FiveLineFile.txt"))
        ResultOutput.writeListToFile(tokens, "src/test/resources/outputs/testFiveLineFile_output.txt")

        Assertions.assertEquals(55, tokens.size)
    }

    @Test
    fun testEmptyLineFile() {
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/EmptyFile.txt"))
        assert(tokens.isEmpty())
    }

    // TODO: AGREGAR TEST PARA TODOS LOS TOKENS
    // TODO: ARREGLAR LOS TESTS Y BORRAR TESTS NO USADOS
}
