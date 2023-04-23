package lexer

import lexer.provider.FileTokenProvider
import org.junit.jupiter.api.Test
import java.io.File

class StreamLexerTest {

    @Test
    fun testTokenExtraction() {
        val file = File("src/test/resources/StreamLexerTest1.txt")
        val tokenProvider = FileTokenProvider(file, "1.1")
        ResultOutput.clearFile("StreamLexerTest1_output.txt")
        repeat(100) {
            ResultOutput.writeLineToFile(tokenProvider.getToken(), "StreamLexerTest1_output.txt")
        }
    }
}
