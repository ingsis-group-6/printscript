package formatter.implementations

import common.config.reader.formatter.FormatterRules
import common.io.Outputter
import common.providers.ast.ASTProvider

class StreamedFormatter(
    private val astProvider: ASTProvider,
    private val outputter: Outputter,
    private val formatter: Formatter
) {
    constructor(astProvider: ASTProvider, outputter: Outputter, configFile: String) :
            this(astProvider, outputter, Formatter(configFile))

    constructor(astProvider: ASTProvider, outputter: Outputter, formatterRules: FormatterRules) :
            this(astProvider, outputter, Formatter(formatterRules))


    fun format() {
        val astProviderResult = astProvider.getAST()
        if (astProviderResult.isPresent) {
            val formatOutput = formatter.format(astProviderResult.get())
            outputter.output(formatOutput)
            if(formatOutput == "EOF") return
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
