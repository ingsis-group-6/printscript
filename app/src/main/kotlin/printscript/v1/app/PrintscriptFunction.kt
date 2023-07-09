package printscript.v1.app

import common.config.reader.formatter.CustomFormatterRules
import common.config.reader.formatter.FormatterRules
import common.io.Inputter
import common.io.Outputter
import formatter.implementations.StreamedFormatter
import interpreter.implementation.StreamInterpreter
import lexer.provider.FileTokenProvider
import linter.implementations.StreamedLinter
import linter.`interface`.Linter
import parser.provider.ASTProvider
import java.io.InputStream

interface PrintscriptStreamedFunction {
    fun execute()
}

class StreamedExecution(sourceFileStream: InputStream, version: String, inputter: Inputter, outputter: Outputter) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileStream, version)
    private val astProvider = ASTProvider(tokenProvider)
    private val streamInterpreter = StreamInterpreter(astProvider, inputter, outputter)

    override fun execute() {
        streamInterpreter.interpret()
    }
}

class StreamedFormat(
    sourceFileInputStream: InputStream,
    configFileName: String,
    version: String,
    outputter: Outputter
) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileInputStream, version)
    private val astProvider = ASTProvider(tokenProvider)
    private var streamedFormatter: StreamedFormatter

    constructor(
        sourceFileInputStream: InputStream,
        version: String,
        outputter: Outputter,
        formatterRules: FormatterRules
    ) : this(sourceFileInputStream, "", version, outputter) {
        streamedFormatter = StreamedFormatter(astProvider, outputter, formatterRules)
    }

    init {
        streamedFormatter = if (configFileName != "") {
            StreamedFormatter(astProvider, outputter, configFileName)
        } else {
            StreamedFormatter(astProvider, outputter, FormatterRules(CustomFormatterRules(0, 0, 0, 0, 0)))
        }
    }

    override fun execute() {
        streamedFormatter.format()
    }
}

class StreamedLint(sourceFileInputStream: InputStream, configFileName: String, version: String, outputter: Outputter) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileInputStream, version)
    private val astProvider = ASTProvider(tokenProvider)
    private var streamedLinter: StreamedLinter

    constructor(sourceFileInputStream: InputStream, version: String, outputter: Outputter, linters: Set<Linter>) : this(sourceFileInputStream, "", version, outputter) {
        streamedLinter = StreamedLinter(astProvider, outputter, linters)
    }

    init {
        streamedLinter = if (configFileName != "") {
            StreamedLinter(astProvider, outputter, configFileName)
        } else {
            StreamedLinter(astProvider, outputter, setOf())
        }
    }

    override fun execute() {
        streamedLinter.lint()
    }
}
