package linter.implementations

import common.ast.AST
import common.config.reader.JsonReader
import common.config.reader.linter.LinterConfigInput
import linter.factory.LinterFactory
import linter.`interface`.Linter

class Linter(configFileName: String) : Linter {

    private val linters: Set<Linter>
    init {
        linters = readRules(configFileName)
    }

    private fun readRules(configFile: String): Set<Linter> {
        val configInput = JsonReader().readJsonFromFile<LinterConfigInput>(configFile)
        return LinterFactory.createSublinters(configInput)

//        return setOf(
//            FunctionExpressionLinter(),
//            IdentifierCaseLinter(CaseConvention.CAMEL_CASE),
//            SyntaxLinter()
//        )
    }

    override fun lint(ast: Pair<AST, List<String>>) {
        linters.forEach { linter: Linter -> linter.lint(ast) }
    }
}
