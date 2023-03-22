package lexer.util

object StringReadingChecker {
    private const val EMPTY_CHAR = '\u0000'
    private var previousChar = EMPTY_CHAR
    private var currentStringClosingChar = EMPTY_CHAR

    fun isPartOfString(currentChar: Char): Boolean {
        when {
            // cuando arranca o cierra el str
            isStringCharacter(currentChar) -> {
                if (currentStringClosingChar == EMPTY_CHAR) { // si se esta abriendo el str
                    currentStringClosingChar = currentChar
                } else if (currentChar == currentStringClosingChar && previousChar != '\\') { // si estoy cerrando el string
                    currentStringClosingChar = EMPTY_CHAR
                    previousChar = EMPTY_CHAR
                }
                return true
            }
            // cuando estas dentro del str
            isInMiddleOfString() -> {
                previousChar = currentChar
                return true
            }
            // cuando no hay string
            else -> return false
        }
    }

    private fun isStringCharacter(currentChar: Char) = currentChar == '"' || currentChar == '\''

    private fun isInMiddleOfString() = currentStringClosingChar != EMPTY_CHAR
}
