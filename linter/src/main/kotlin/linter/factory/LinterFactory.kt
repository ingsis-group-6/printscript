package linter.factory

import linter.`interface`.Linter

interface LinterFactory<T> {
    fun createSublinters(configInput: T): Set<Linter>
}
