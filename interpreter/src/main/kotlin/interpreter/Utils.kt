package interpreter

import common.token.TokenType

object Utils {
    fun checkIfInteger(rhsValue: Pair<String?, TokenType>): Pair<String?, TokenType> {
        var rhsValue1 = rhsValue
        if (rhsValue1.second == TokenType.NUMERIC_LITERAL) {
            val parsed = rhsValue1.first!!.toDouble()
            rhsValue1 = if (isWhole(parsed)) Pair(parsed.toInt().toString(), TokenType.NUMERIC_LITERAL) else rhsValue1
        }
        return rhsValue1
    }

    private fun isWhole(parsed: Double) = parsed % 1 == 0.0
}
