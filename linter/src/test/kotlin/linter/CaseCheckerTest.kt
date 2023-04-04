package linter

import linter.implementations.CaseChecker
import linter.implementations.CaseConvention
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CaseCheckerTest {
    private val checker = CaseChecker()
    @Test
    fun testCamelCase() {
        val case = CaseConvention.CAMEL_CASE
        assertEquals(false, checker.checkCase(case,"hello_world"))
        assertEquals(false, checker.checkCase(case,"HelloWorld_"))
        assertEquals(false, checker.checkCase(case,"HelloWorld"))
        assertEquals(true, checker.checkCase(case,"helloWorld"))
        assertEquals(true, checker.checkCase(case,"camelCaseTest"))
        assertEquals(true, checker.checkCase(case,"hello"))
        assertEquals(false, checker.checkCase(case,""))
        assertEquals(false, checker.checkCase(case,"camelCaseTest_"))
    }

    @Test
    fun testSnakeCase() {
        val case = CaseConvention.SNAKE_CASE
        assertEquals(true, checker.checkCase(case,"hello_world"))
        assertEquals(false, checker.checkCase(case,"HelloWorld_"))
        assertEquals(false, checker.checkCase(case,"camelCaseTest"))
        assertEquals(true, checker.checkCase(case,"snake_case_test"))
    }
}