package linter.implementations

import common.ast.AST
import common.ast.implementations.asts.EndOfFileAST
import common.config.reader.JsonReader
import common.config.reader.linter.LinterConfigInput
import common.io.Outputter
import linter.factory.LinterFactory
import linter.factory.LinterFileConfigFactory
import linter.`interface`.Linter
import kotlin.system.exitProcess

class LinterImpl(private val linters: Set<Linter>) : Linter {

    constructor(configFileName: String) : this(readRules(configFileName))

    companion object {
        private fun readRules(configFile: String): Set<Linter> {
            val configInput = JsonReader().readJsonFromFile<LinterConfigInput>(configFile)
            val linterFactory = LinterFileConfigFactory()
            return linterFactory.createSublinters(configInput)
        }
    }


    override fun lint(ast: Pair<AST, List<String>>, outputter: Outputter) {
        if (ast.first is EndOfFileAST) exitProcess(0)
        linters.forEach { linter: Linter -> linter.lint(ast, outputter) }
    }
}