package interpreter.input

import common.io.Inputter

class ConsoleInputter : Inputter {
    override fun getInputLine(): String? {
        return readlnOrNull()
    }
}
