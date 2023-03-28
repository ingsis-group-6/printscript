package parser.implementation

import java.util.*

class ShuntingYard {

    fun shuntingYard(tokens: List<String>): TreeNode? {
        val stack = Stack<String>()
        val valueQueue: Queue<String> = LinkedList()

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> valueQueue.add(token)
                token in listOf("+", "-", "*", "/") -> {
                    while (!stack.isEmpty() && stack.peek() in listOf("*", "/") && stack.peek() != "(") {
                        valueQueue.add(stack.pop()!!)
                    }
                    stack.push(token)
                }
                token == "(" -> stack.push(token)
                token == ")" -> {
                    while (stack.peek() != "(") {
                        valueQueue.add(stack.pop()!!)
                    }
                    stack.pop()
                }
            }
        }

        while (!stack.isEmpty()) {
            valueQueue.add(stack.pop()!!)
        }

        val treeStack = Stack<TreeNode>()

        for (element in valueQueue) {
            if (element.toDoubleOrNull() != null) {
                treeStack.push(TreeNode(element))
            } else {
                val right = treeStack.pop()
                val left = treeStack.pop()
                treeStack.push(TreeNode(element, left, right))
            }
        }

        return treeStack.pop()
    }
}

data class TreeNode(val headToken: String, val left: TreeNode? = null, val right: TreeNode? = null)
