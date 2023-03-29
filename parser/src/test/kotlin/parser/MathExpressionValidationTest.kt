package parser

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MathExpressionValidationTest {
    fun isMathExpressionCorrect(expression: String): Boolean {
        val stack = mutableListOf<Char>()

        for (i in expression.indices) {
            val c = expression[i]
            when (c) {
                '(' -> stack.add(c)
                ')' -> if (stack.isNotEmpty() && stack.last() == '(') {
                    stack.removeAt(stack.lastIndex)
                } else {
                    return false
                }
                in setOf('+', '-', '*', '/') -> {
                    if (i == 0 || i == expression.lastIndex) {
                        return false // operator cannot be at beginning or end of expression
                    } else if (expression[i - 1] in setOf('+', '-', '*', '/')) {
                        return false // consecutive operators
                    }
                }
                in setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', ' ') -> {} // digits and whitespace are valid
                else -> return false // invalid character
            }
        }

        return stack.isEmpty()
    }

//    fun isValidMathExpression(tokens: List<String>): Boolean {
//        val stack = mutableListOf<String>()
//
//        for (i in tokens.indices) {
//            val token = tokens[i]
//            when {
//                token == "(" -> stack.add(token)
//                token == ")" -> if (stack.isNotEmpty() && stack.last() == "(") {
//                    stack.removeAt(stack.lastIndex)
//                } else {
//                    return false
//                }
//                token in setOf("+", "-", "*", "/") -> {
//                    if (i == 0 || i == tokens.lastIndex) {
//                        return false // operator cannot be at beginning or end of expression
//                    } else if (tokens[i - 1] in setOf("+", "-", "*", "/")) {
//                        return false // consecutive operators
//                    }
//                }
//                //token.matches(Regex("[0-9.]+")) -> {} // digits and decimal point are valid
//                else -> return true // invalid character
//            }
//        }
//
//        return stack.isEmpty()
//    }

    fun isValidMathExpression(tokens: List<String>): Boolean {
        val stack = mutableListOf<String>()
        var prevToken = ""

        for (i in tokens.indices) {
            val token = tokens[i]
            when {
                token == "(" -> {
                    if (prevToken.isNotEmpty() && (prevToken.matches(Regex("[0-9.]+")) || prevToken == ")")) {
                        return false // opening parenthesis cannot follow a number, decimal point, or closing parenthesis
                    }
                    stack.add(token)
                    prevToken = ""
                }
                token == ")" -> {
                    if (stack.isNotEmpty() && stack.last() == "(" && prevToken != "(" && prevToken != "") {
                        stack.removeAt(stack.lastIndex)
                        prevToken = ")"
                    } else {
                        return false
                    }
                }
                token in setOf("+", "-", "*", "/") -> {
                    if (i == 0 || i == tokens.lastIndex || prevToken.matches(Regex("[+\\-*/]")) || prevToken == "(") {
                        return false // operator cannot be at beginning or end of expression, or follow another operator or opening parenthesis
                    } else {
                        prevToken = token
                    }
                }
                token.matches(Regex("[a-zA-Z][a-zA-Z0-9]*|[0-9.]+")) -> {
                    if (prevToken.isNotEmpty() && (prevToken.matches(Regex("[a-zA-Z][a-zA-Z0-9]*")) || prevToken.matches(Regex("[0-9.]+")))) {
                        return false // variables and numbers cannot be consecutive
                    }
                    prevToken = token
                }
                else -> return false // invalid character
            }
        }

        return stack.isEmpty() && prevToken != "" && prevToken != "(" && prevToken != "." // last token must be a number, variable, or closing parenthesis
    }



    @Test
    fun testValidExpression() {
        val expression = "2 * (4 + 6) / 3 - 5"
        assertTrue(isMathExpressionCorrect(expression))
    }

    @Test
    fun testInvalidExpression() {
        val expression = "2 * (4 + 6 / 3 - 5"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testConsecutiveOperators() {
        val expression = "6 ++ 4"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testOperatorAtBeginning() {
        val expression = "+6 * 4"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testOperatorAtEnd() {
        val expression = "6 * 4-"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testInvalidCharacter() {
        val expression = "6 @ 4"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testEmptyExpression() {
        val expression = ""
        assertTrue(isMathExpressionCorrect(expression))
    }

    @Test
    fun testOnlyDigitsAndWhitespace() {
        val expression = "6 4 3 7  2  9 1 0"
        assertTrue(isMathExpressionCorrect(expression))
    }

    @Test
    fun testConsecutiveOperatorsAndParenthesis() {
        val expression = "( 6 - 2 - 3)((9*9) + 3 - 4 )"
        assertTrue(isMathExpressionCorrect(expression))
    }

    @Test
    fun testNonClosingParenthesis() {
        val expression = "( 6 - 2 - 3))(()((9*9) + 3 - 4 )"
        assertFalse(isMathExpressionCorrect(expression))
    }

    @Test
    fun testValidExpression2() {
        val tokens = listOf("(", "5", "+", "aVar", ")", "*", "2")
        assertTrue(isValidMathExpression(tokens))
    }

    @Test
    fun testInvalidExpressionWithMismatchedParentheses() {
        val tokens = listOf("(", "5", "+", "3", "*", "2")
        assertFalse(isValidMathExpression(tokens))
    }

    @Test
    fun testInvalidExpressionWithConsecutiveOperators() {
        val tokens = listOf("5", "+", "+", "3", "*", "2")
        assertFalse(isValidMathExpression(tokens))
    }

    @Test
    fun testInvalidExpressionWithOperatorAtBeginning() {
        val tokens = listOf("+", "5", "*", "3", "/", "2")
        assertFalse(isValidMathExpression(tokens))
    }

    @Test
    fun testInvalidExpressionWithOperatorAtEnd() {
        val tokens = listOf("5", "*", "3", "/", "2", "+")
        assertFalse(isValidMathExpression(tokens))
    }

    @Test
    fun testInvalidExpressionWithInvalidCharacter() {
        val tokens = listOf("5", "%", "3", "/", "2")
        assertFalse(isValidMathExpression(tokens))
    }

    @Test
    fun testValidMathExpressionWithNumberLiterals() {
        assertTrue(isValidMathExpression(listOf("5", "+", "3", "*", "4", "/", "2")))
        assertTrue(isValidMathExpression(listOf("(", "5", "+", "3", ")", "*", "4", "/", "2")))
        assertTrue(isValidMathExpression(listOf("5", "*", "(", "3", "+", "4", ")", "/", "2")))
        assertTrue(isValidMathExpression(listOf("5", "+", "3.2", "*", "4", "/", "2")))
    }

    @Test
    fun testValidMathExpressionWithVariableLiterals() {
        assertTrue(isValidMathExpression(listOf("myVar", "+", "otherVar", "*", "3")))
        assertTrue(isValidMathExpression(listOf("(", "var1", "-", "var2", ")", "*", "var3")))
        assertTrue(isValidMathExpression(listOf("varA", "+", "varB", "*", "varC", "/", "varD")))
    }

    @Test
    fun testInvalidMathExpressionWithConsecutiveLiterals() {
        assertFalse(isValidMathExpression(listOf("5", "myVar", "+", "3")))
        assertFalse(isValidMathExpression(listOf("(", "var1", "otherVar", "-", "var2", ")", "*", "var3")))
        assertFalse(isValidMathExpression(listOf("varA", "+", "varB", "varC", "/", "varD")))
    }

    @Test
    fun testInvalidMathExpressionWithMismathchedParenthesis() {
        assertFalse(isValidMathExpression(listOf("(", "5", "+", "3", ")", "*", "4", "/", "2", ")")))
        assertFalse(isValidMathExpression(listOf("(", "5", "+", "3", "*", "4", "/", "2")))
        assertFalse(isValidMathExpression(listOf("5", "+", "3", "*", "4", "/", "2", ")")))
        assertFalse(isValidMathExpression(listOf("(", "5", "+", "3", "*", "4", "/", "2")))
    }

    @Test
    fun testInvalidMathExpressionWithIncorrectCharacters() {
        assertFalse(isValidMathExpression(listOf("5", "+", "3", "a", "*", "4", "/", "2")))
        assertFalse(isValidMathExpression(listOf("5", "+", "3", "@", "4", "/", "2")))
    }

    @Test
    fun testInvalidMathExpressionWithConsecutiveOperators() {
        assertFalse(isValidMathExpression(listOf("5", "++", "3")))
        assertFalse(isValidMathExpression(listOf("5", "+", "-", "3")))
        assertFalse(isValidMathExpression(listOf("5", "+", "3", "*", "/", "4", "/", "2")))
    }
}
