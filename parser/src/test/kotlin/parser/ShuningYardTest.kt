package parser

import java.util.*
import kotlin.math.pow

sealed class MathOperation {
    data class Number(val value: Double) : MathOperation()
    data class Operator(val symbol: Char, val precedence: Int, val associativity: Associativity) : MathOperation() {
        enum class Associativity { LEFT, RIGHT }
        fun apply(left: Double, right: Double) = when (symbol) {
            '+' -> left + right
            '-' -> left - right
            '*' -> left * right
            '/' -> left / right
            '^' -> left.pow(right)
            else -> throw IllegalArgumentException("Invalid operator: $symbol")
        }
    }
}

fun String.toMathOperation(): MathOperation {
    val trimmed = this.trim()
    return when {
        trimmed.matches(Regex("[+\\-*/^]")) -> MathOperation.Operator(trimmed[0], getPrecedence(trimmed[0]), MathOperation.Operator.Associativity.LEFT)
        trimmed.matches(Regex("-?\\d+(\\.\\d+)?")) -> MathOperation.Number(trimmed.toDouble())
        else -> throw IllegalArgumentException("Invalid input: $trimmed")
    }
}

fun getPrecedence(operator: Char): Int = when (operator) {
    '+', '-' -> 1
    '*', '/' -> 2
    '^' -> 3
    else -> throw IllegalArgumentException("Invalid operator: $operator")
}

fun infixToPostfix(infix: String): List<MathOperation> {
    val outputQueue = mutableListOf<MathOperation>()
    val operatorStack = Stack<MathOperation.Operator>()

    infix.split(" ").forEach { token ->
        when (val operation = token.toMathOperation()) {
            is MathOperation.Number -> outputQueue.add(operation)
            is MathOperation.Operator -> {
                while (operatorStack.isNotEmpty()) {
                    val top = operatorStack.peek() as? MathOperation.Operator ?: break
                    if ((operation.associativity == MathOperation.Operator.Associativity.LEFT && operation.precedence <= top.precedence)
                        || (operation.associativity == MathOperation.Operator.Associativity.RIGHT && operation.precedence < top.precedence)) {
                        outputQueue.add(operatorStack.pop())
                    } else break
                }
                operatorStack.push(operation)
            }
        }
    }

    while (operatorStack.isNotEmpty()) {
        outputQueue.add(operatorStack.pop())
    }

    return outputQueue
}

fun postfixToTree(postfix: List<MathOperation>): MathOperation {
    val stack = Stack<MathOperation>()
    postfix.forEach { token ->
        when (token) {
            is MathOperation.Number -> stack.push(token)
            is MathOperation.Operator -> {
                val right = stack.pop() as MathOperation.Number
                val left = stack.pop() as MathOperation.Number
                stack.push(MathOperation.Number(token.apply(left.value, right.value)))
            }
        }
    }
    return stack.pop()
}

fun main() {
    val input = "5 + (3 * 2)"
    val postfix = infixToPostfix(input)
    val tree = postfixToTree(postfix)
    println(tree)
}
