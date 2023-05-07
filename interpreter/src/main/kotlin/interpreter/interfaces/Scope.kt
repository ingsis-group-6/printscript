package interpreter.interfaces

interface Scope {

    fun findVariableData(identifier: String, currentLine: Int): Pair<String, String?>
    fun getMutableVariables(): Map<String, Pair<String, String?>>
    fun getImmutableVariables(): Map<String, Pair<String, String?>>
    fun putMutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int)
    fun putImmutableVariable(identifier: String, variable: Pair<String, String?>, currentLine: Int)
    fun existsVariable(identifier: String?): Boolean
    fun getPreviousScope(): Scope
    fun existsMutableVariable(identifier: String?): Boolean
    fun existsImmutableVariable(identifier: String?): Boolean
    fun getAllVariables(): Map<String, Pair<String, String?>>
}
