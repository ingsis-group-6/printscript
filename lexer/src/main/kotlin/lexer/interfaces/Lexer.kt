package lexer.interfaces

import lexer.token.Token
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader

interface Lexer {

    /**
     * https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html
     *
     * un InputStreamReader va dando cada character con read().
     *
     */

    fun extractTokens(inputData: InputStreamReader): InputStream

    /**
     * 1) lees 1 linea
     * 2) sacas la Lista de Tokens de esa linea
     * 3) escribir en un file
     * 4)
     *
     *
     */



}

