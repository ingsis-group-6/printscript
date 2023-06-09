package linter.factory

import common.config.reader.linter.LinterConfigInput
import linter.implementations.FunctionExpressionLinter
import linter.implementations.IdentifierCaseLinter
import linter.`interface`.Linter

object LinterFactory {

    fun createSublinters(configInput: LinterConfigInput?): Set<Linter> {
        if (configInput == null) return setOf()
        val setToReturn = mutableSetOf<Linter>()
        setToReturn.add(IdentifierCaseLinter(configInput.caseConvention))
        if (!configInput.printExpressionsEnabled) setToReturn.add(FunctionExpressionLinter())
        return setToReturn.toSet()
    }
}
