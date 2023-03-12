package lexer

import lexer.implementation.LexerImpl
import lexer.implementation.TokenTypeList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class LexerTest {

    private val lexer = LexerImpl(TokenTypeList(listOf("let","string","number")), listOf(' '), listOf(';',':'))

    @Test
    fun testOneLineFile1(){
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/OneLineFile1.txt"))
        Assertions.assertEquals(7, tokens.size)
    }

    @Test
    fun testOneLineFile2(){
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/OneLineFile2.txt"))
        tokens.map { println(it) }
        Assertions.assertEquals(7, tokens.size)
    }

    @Test
    fun testFiveLineFile(){
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/FiveLineFile.txt"))
        Assertions.assertEquals(35, tokens.size)
    }

    @Test
    fun testEmptyLineFile(){
        val tokens = lexer.extractTokensFromFile(File("src/test/resources/ZeroLineFile.txt"))
        assert(tokens.isEmpty())
    }



    //TODO: AGREGAR TEST PARA TODOS LOS TOKENS
    //TODO: ARREGLAR LOS TESTS Y BORRAR TESTS NO USADOS
}
