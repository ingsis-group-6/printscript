package linter.implementations

import common.ast.implementations.asts.EndOfFileAST
import common.io.Outputter
import common.providers.ast.ASTErrorReporter
import linter.`interface`.Linter

class StreamedLinter(private val astErrorReporter: ASTErrorReporter, private val outputter: Outputter, configFile: String) {
    private var linter = LinterImpl(configFile)

    constructor(astErrorReporter: ASTErrorReporter, outputter: Outputter, linters: Set<Linter>) : this(astErrorReporter, outputter, "") {
        linter = LinterImpl(linters)
    }

    fun lint() {
        val astProviderResult = astErrorReporter.checkASTCreation()
        if (astProviderResult.isPresent) {
            if (astProviderResult.get().first is EndOfFileAST) return
            linter.lint(astProviderResult.get(), outputter)
        }
        lint()
    }
}
