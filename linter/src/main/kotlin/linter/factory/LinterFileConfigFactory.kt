package linter.factory

import common.config.reader.linter.LinterConfigInput
import linter.implementations.FunctionExpressionLinter
import linter.implementations.IdentifierCaseLinter
import linter.implementations.SyntaxLinter
import linter.`interface`.Linter

class LinterFileConfigFactory: LinterFactory<LinterConfigInput?> {

    override fun createSublinters(configInput: LinterConfigInput?): Set<Linter> {
        if (configInput == null) return setOf()
        val setToReturn = mutableSetOf<Linter>()
        setToReturn.add(IdentifierCaseLinter(configInput.caseConvention))
        if (!configInput.printExpressionsEnabled) setToReturn.add(FunctionExpressionLinter())
        setToReturn.add(SyntaxLinter())
        return setToReturn.toSet()
    }
}
