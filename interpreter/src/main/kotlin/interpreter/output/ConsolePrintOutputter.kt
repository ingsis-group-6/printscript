package interpreter.output

class ConsolePrintOutputter : Outputter {

    override fun output(text: String) {
        println(text)
    }
}
