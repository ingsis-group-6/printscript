package linter.factory

import common.config.reader.linter.LinterConfigInput
import linter.`interface`.Linter

interface LinterFactory<T> {
    fun createSublinters(configInput: T): Set<Linter>
}