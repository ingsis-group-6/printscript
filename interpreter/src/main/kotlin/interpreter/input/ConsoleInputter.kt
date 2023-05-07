package interpreter.input

class ConsoleInputter : Inputter {
    override fun getInputLine(): String? {
        return readlnOrNull()
    }
}
