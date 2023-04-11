import common.ast.implementations.asts.EmptyAST
import linter.implementations.Linter
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LinterTests {

    @Test
    fun createLinter() {
        val linter = Linter("../linter_config.json")
        val linterSet = linter.getSubLinter()
        Assertions.assertTrue(linterSet.size > 0)
    }

    @Test
    fun lintTest() {
        val linter = Linter("../linter_config.json")

        linter.lint(Pair(EmptyAST, listOf("test")))

    }
}