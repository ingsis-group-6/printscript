package linter.implementations

import common.ast.implementations.asts.EndOfFileAST
import common.io.Outputter
import common.providers.ast.ASTErrorReporter
import linter.`interface`.Linter

class StreamedLinter(private val astErrorReporter: ASTErrorReporter, private val outputter: Outputter, private val linter: Linter) {

    constructor(astErrorReporter: ASTErrorReporter, outputter: Outputter, configFile: String) :
        this(astErrorReporter, outputter, LinterImpl(configFile))

    constructor(astErrorReporter: ASTErrorReporter, outputter: Outputter, linters: Set<Linter>) :
        this(astErrorReporter, outputter, LinterImpl(linters))

    fun lint() {
        val astProviderResult = astErrorReporter.checkASTCreation()
        if (astProviderResult.isPresent) {
            if (astProviderResult.get().first is EndOfFileAST) return
            linter.lint(astProviderResult.get(), outputter)
        }
        lint()
    }
}
