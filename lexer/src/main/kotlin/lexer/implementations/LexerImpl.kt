package lexer.implementations

import lexer.interfaces.Lexer
import java.io.InputStream
import java.io.InputStreamReader

class LexerImpl: Lexer {
    /**
     * // App
     * 1) Leer file
     * 2) Crear el InputStreamReader
     * //
     *
     *
     * 1) lees 1 linea (X lineas)
     * 2) sacas la Lista de Tokens de esa linea
     * 3) escribir en un file
     * 4)
     *
     *
     */


    override fun extractTokens(inputData: InputStreamReader): InputStream {
        getLineBuffer(inputData)


    }

    fun getLineBuffer(inputData: InputStreamReader): String {
        val stopChars = listOf(';') // La idea es tener un array y que se puedan ampliar los stopchars posibles
        var soFar = ""
        val bufferLines = 5
        var counter = 0

        while (inputData.ready() || counter == bufferLines) {
            val currentChar = inputData.read()
            if (stopChars.contains(currentChar as Char)) {
                counter++
            }
            soFar = soFar + currentChar
        }
        return soFar
    }


}