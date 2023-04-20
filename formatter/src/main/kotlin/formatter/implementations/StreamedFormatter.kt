package formatter.implementations

import common.providers.ast.ASTProvider

class StreamedFormatter(
    private val astProvider: ASTProvider,
    private val ftw: FormattedTextWriter,
    configFile: String
) {
    private val formatter = Formatter(configFile)

    fun format() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            ftw.writeLine(formatter.format(astProviderResult.get()))
        }
        format()
    }

    /**
     * Este formatter rompe si el ast esta mal formado.
     *
     * Antes al tener el Tokens.txt, borramos todo_el source file y luego vamos rearmando de ahi
     *
     * Ahora, al no tener todos los tokens, al borrar el src file, se borran todos los tokens que vienen
     * despues y no hay nada mas con que armar el file.
     *
     *
     */
}
