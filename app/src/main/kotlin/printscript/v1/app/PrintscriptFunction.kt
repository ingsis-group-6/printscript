package printscript.v1.app

import common.token.Token
import formatter.implementations.FormattedTextWriter
import formatter.implementations.Formatter
import formatter.implementations.StreamedFormatter
import interpreter.EmptyScope
import interpreter.Scope
import interpreter.implementation.Interpreter
import interpreter.implementation.StreamInterpreter
import interpreter.output.Outputter
import lexer.provider.FileTokenProvider
import linter.implementations.Linter
import linter.implementations.StreamedLinter
import parser.implementation.Parser
import parser.provider.ASTProvider
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

interface PrintscriptFunction {
    fun execute(tokenLine: List<Token>)
}

class ExecuteFunction : PrintscriptFunction {
    private val parser = Parser()
    private val interpreter = Interpreter(Scope(mutableMapOf(), mutableMapOf(), EmptyScope))
    override fun execute(tokenLine: List<Token>) = interpreter.interpret(parser.parse(tokenLine))
}

class LinterFunction(configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val linter = Linter(configFileName)
    override fun execute(tokenLine: List<Token>) {
        linter.lint(parser.check(tokenLine))
    }
}

class FormatFunction(fileToWrite: String, configFileName: String) : PrintscriptFunction {
    private val parser = Parser()
    private val formatter = Formatter(configFileName)
    private val ftw = FormattedTextWriter(File(fileToWrite))
    override fun execute(tokenLine: List<Token>) {
        ftw.writeLine(formatter.format(parser.parse(tokenLine)))
    }
}

interface PrintscriptStreamedFunction {
    fun execute()
}

class StreamedExecution(sourceFileStream: InputStream, version: String, outputter: Outputter) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(sourceFileStream, version)
    private val astProvider = ASTProvider(tokenProvider)
    private val streamInterpreter = StreamInterpreter(astProvider, outputter)

    override fun execute() {
        streamInterpreter.interpret()
    }
}

class StreamedFormat(sourceFile: File, configFileName: String, version: String) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(FileInputStream(sourceFile), version)
    private val astProvider = ASTProvider(tokenProvider)
    private val ftw = FormattedTextWriter(sourceFile)
    private val streamedFormatter = StreamedFormatter(astProvider, ftw, configFileName)
    override fun execute() {
        streamedFormatter.format()
    }
}

class StreamedLint(sourceFile: File, configFileName: String, version: String) : PrintscriptStreamedFunction {
    private val tokenProvider = FileTokenProvider(FileInputStream(sourceFile), version)
    private val astProvider = ASTProvider(tokenProvider)
    private val streamedLinter = StreamedLinter(astProvider, configFileName)
    override fun execute() {
        streamedLinter.lint()
    }
}
