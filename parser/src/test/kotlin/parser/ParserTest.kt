package parser

import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class ParserTest {

    @Test
    fun testJsonParse(){

        val s: String = "{\"name\":\"John\", \"age\":30}"
        val inputStream: InputStream = s.byteInputStream(StandardCharsets.UTF_8);
    }
}