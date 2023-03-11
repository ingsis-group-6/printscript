package lexer

import lexer.implementations.LexerImpl
import lexer.utils.InputStreamUtils
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



    }



}
