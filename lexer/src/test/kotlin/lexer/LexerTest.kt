package lexer

import lexer.implementations.LexerImpl
import lexer.utils.InputStreamUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class LexerTest {

    @Test
    fun testEmptyInput(){
        val lexer = LexerImpl()
        val input = ""
        val result = lexer.extractTokens(InputStreamReader(InputStreamUtils.convertStringToInputStream(input)))
    }

    @Test
    fun testLineBuffer() {
        val string1 = "let a: number = 21;"
        val input1 = InputStreamReader(InputStreamUtils.convertStringToInputStream(string1))

        val stringMore = "let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;"
        val inputMore = InputStreamReader(InputStreamUtils.convertStringToInputStream(stringMore))

        val string5lines = "let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;let a: number = 21;"
        val input5lines = InputStreamReader(InputStreamUtils.convertStringToInputStream(string5lines))

        val empty = ""
        val emptyInput = InputStreamReader(InputStreamUtils.convertStringToInputStream(empty))

        val lexer = LexerImpl()

        val res1 = lexer.getLineBuffer(input1)
        Assertions.assertEquals(string1, res1)

        val res2 = lexer.getLineBuffer(input5lines)
        Assertions.assertEquals(string5lines, res2)

        val res3 = lexer.getLineBuffer(inputMore)
        Assertions.assertNotEquals(stringMore, res3)



    }

    @Test
    fun testInputStreamReader(){
        val str = "ab"
        val aInStr = InputStreamUtils.convertStringToInputStream(str)
        val stringRead = "" + aInStr.read().toChar() + aInStr.read().toChar()

        Assertions.assertEquals(str, stringRead)
    }



}
