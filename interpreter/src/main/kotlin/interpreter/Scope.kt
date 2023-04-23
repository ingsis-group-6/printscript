package interpreter

import interpreter.interfaces.Scope
data class Scope(
    private val mutableSymbolTable: MutableMap<String, Pair<String, String?>>,
    private val immutableSymbolTable: MutableMap<String, Pair<String, String?>>,
    private val previousScope: Scope
) : Scope {

    override fun findVariableData(identifier: String, currentLine: Int): Pair<String, String?> {
        val valueFromMutableSymbolTable = mutableSymbolTable[identifier]
        if (valueFromMutableSymbolTable != null) {
            return valueFromMutableSymbolTable
        }

        val valueFromImmutableSymbolTable = immutableSymbolTable[identifier]
        if (valueFromImmutableSymbolTable != null) {
            return valueFromImmutableSymbolTable
        }
        return previousScope.findVariableData(identifier, currentLine)
    }

    override fun getMutableVariables(): Map<String, Pair<String, String?>> {
        val copyMap = HashMap(mutableSymbolTable)
        copyMap.putAll(previousScope.getMutableVariables())
        return copyMap
    }

    override fun getImmutableVariables(): Map<String, Pair<String, String?>> {
        val copyMap = HashMap(immutableSymbolTable)
        copyMap.putAll(previousScope.getImmutableVariables())
        return copyMap
    }

    override fun putMutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int) {
        if (existsMutableVariable(identifier)) {
            if (mutableSymbolTable.contains(identifier)) {
                mutableSymbolTable[identifier] = variable
            } else {
                previousScope.putMutableVariable(identifier, variable, currentLine)
            }
        } else {
            mutableSymbolTable[identifier] = variable
        }
    }

    override fun putImmutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int) {
        if (existsImmutableVariable(identifier)) {
            if (immutableSymbolTable.contains(identifier) && immutableSymbolTable[identifier]!!.second == null) {
                immutableSymbolTable[identifier] = variable
            } else {
                previousScope.putImmutableVariable(identifier, variable, currentLine)
            }
        } else {
            immutableSymbolTable[identifier] = variable
        }
    }

    override fun existsVariable(identifier: String?): Boolean {
        return existsMutableVariable(identifier) || existsImmutableVariable(identifier)
    }

    override fun existsImmutableVariable(identifier: String?): Boolean {
        return if (immutableSymbolTable.contains(identifier)) {
            true
        } else {
            previousScope.existsImmutableVariable(identifier)
        }
    }

    override fun existsMutableVariable(identifier: String?): Boolean {
        return if (mutableSymbolTable.contains(identifier)) {
            true
        } else {
            previousScope.existsMutableVariable(identifier)
        }
    }

    override fun getPreviousScope(): Scope {
        return previousScope
    }

    override fun getAllVariables(): Map<String, Pair<String, String?>> {
        val copyMap = HashMap(getMutableVariables())
        copyMap.putAll(getImmutableVariables())
        return copyMap
    }
}

object EmptyScope : Scope {
    override fun findVariableData(identifier: String, currentLine: Int): Pair<String, String?> {
        throw Exception("($currentLine) - Identifier $identifier not declared.")
    }

    override fun getMutableVariables(): Map<String, Pair<String, String?>> {
        return mapOf<String, Pair<String, String?>>()
    }

    override fun getImmutableVariables(): Map<String, Pair<String, String?>> {
        return mapOf<String, Pair<String, String?>>()
    }

    override fun putMutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int) {
        throw Exception("($currentLine) - Out of scope")
    }

    override fun putImmutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int) {
        throw Exception("($currentLine) - Out of scope")
    }

    override fun existsVariable(identifier: String?): Boolean {
        return false
    }

    override fun getPreviousScope(): Scope {
        throw Exception("No previous scope.")
    }

    override fun existsMutableVariable(identifier: String?): Boolean {
        return false
    }

    override fun existsImmutableVariable(identifier: String?): Boolean {
        return false
    }

    override fun getAllVariables(): Map<String, Pair<String, String?>> {
        return mapOf<String, Pair<String, String?>>()
    }
}
