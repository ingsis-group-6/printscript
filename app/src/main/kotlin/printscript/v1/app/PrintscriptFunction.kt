package printscript.v1.app

import formatter.implementations.StreamedFormatter
import interpreter.implementation.StreamInterpreter
import common.io.Inputter
import common.io.Outputter
import lexer.provider.FileTokenProvider
import linter.implementations.StreamedLinter
import parser.provider.ASTProvider
import java.io.File
import java.io.FileInputStream
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

class StreamedFormat(sourceFileInputStream: InputStream, configFileName: String, version: String, outputter: Outputter) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileInputStream, version)
    private val astProvider = ASTProvider(tokenProvider)

    private val streamedFormatter = StreamedFormatter(astProvider, outputter, configFileName)
    override fun execute() {
        streamedFormatter.format()
    }
}

class StreamedLint(sourceFileInputStream: InputStream, configFileName: String, version: String) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileInputStream, version)
    private val astProvider = ASTProvider(tokenProvider)
    private val streamedLinter = StreamedLinter(astProvider, configFileName)
    override fun execute() {
        streamedLinter.lint()
    }
}
