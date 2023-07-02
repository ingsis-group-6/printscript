package printscript.v1.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import common.config.reader.formatter.CustomFormatterRules
import common.config.reader.formatter.FormatterRules
import common.config.reader.linter.CaseConvention
import common.io.Outputter
import formatter.implementations.FormattedTextWriter
import interpreter.input.ConsoleInputter
import interpreter.output.ConsolePrintOutputter
import linter.implementations.IdentifierCaseLinter
import printscript.v1.app.StreamedExecution
import printscript.v1.app.StreamedFormat
import printscript.v1.app.StreamedLint
import java.io.File
import java.io.FileInputStream
import java.util.*

class PrintScript : CliktCommand(name = "printscript", help = "Printscript") {
    override fun run() = Unit
}
class Run : CliktCommand(help = "Run a Printscript file") {

    private val sourceFile by argument(help = "The source file to run")
    private val version by argument(help = "Printscript version to run (optional)").optional()
    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), ExecuteFunction())
        println("Current version: $version")
        StreamedExecution(FileInputStream(File(sourceFile)), version.toString(), ConsoleInputter(), ConsolePrintOutputter()).execute()
    }
}

class Lint : CliktCommand(help = "Lint a Printscript file") {

    private val sourceFile by argument(help = "The source file to lint")
    private val configFile by argument(help = "The config file to use")
    private val version by argument(help = "Printscript version to run (optional)").optional()
    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), LinterFunction(configFile))
        println(File(sourceFile))
        //StreamedLint(FileInputStream(File(sourceFile)), configFile, version.toString(), ConsolePrintOutputter()).execute()
        StreamedLint(FileInputStream(File(sourceFile)), version.toString(), ConsolePrintOutputter(), setOf(
            IdentifierCaseLinter(CaseConvention.SNAKE_CASE)

        )).execute()
    }
}

class Format : CliktCommand(help = "Format a Printscript file") {

    private val sourceFile by argument(help = "The source file to format")
    private val configFile by argument(help = "The config file to use")
    private val version by argument(help = "Printscript version to run (optional)").optional()

    override fun run() {
        // CLIUtils.runAppWithFunction(File(sourceFile), FormatFunction(sourceFile, configFile))
        val outputter: Outputter = FormattedTextWriter(File(sourceFile))
        val rules = FormatterRules(CustomFormatterRules(0,5,5,1,4))
        //StreamedFormat(FileInputStream(File(sourceFile)), configFile, version.toString(), outputter).execute()
        StreamedFormat(FileInputStream(File(sourceFile)), version.toString(), outputter, rules).execute()
    }
}
