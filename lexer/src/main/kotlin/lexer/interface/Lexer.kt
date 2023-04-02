package lexer.`interface`

import java.io.File

interface Lexer {

    /**
     * https://docs.oracle.com/javase/8/docs/api/java/io/InputStreamReader.html
     *
     * un InputStreamReader va dando cada character con read().
     *
     */

    fun extractTokensFromFile(file: File): Unit

    /**
     * 1) lees 1 linea
     * 2) sacas la Lista de Tokens de esa linea
     * 3) escribir en un file
     * 4)
     *
     *
     */
}
