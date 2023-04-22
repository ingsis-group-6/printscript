package linter.implementations

import common.providers.ast.ASTErrorReporter

class StreamedLinter(private val astErrorReporter: ASTErrorReporter, configFile: String) {
    private val linter = Linter(configFile)

    fun lint() {
        val astProviderResult = astErrorReporter.checkASTCreation()
        if (astProviderResult.isPresent) {
            linter.lint(astProviderResult.get())
        }
        lint()
    }
}
