package interpreter.output

import common.io.Outputter

class ConsolePrintOutputter : Outputter {

    override fun output(text: String) {
        println(text)
    }
}
