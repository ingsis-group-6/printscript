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
        assertEquals(false, checker.isValidCase(case, "hello_world"))
        assertEquals(false, checker.isValidCase(case, "HelloWorld_"))
        assertEquals(false, checker.isValidCase(case, "HelloWorld"))
        assertEquals(true, checker.isValidCase(case, "helloWorld"))
        assertEquals(true, checker.isValidCase(case, "camelCaseTest"))
        assertEquals(true, checker.isValidCase(case, "hello"))
        assertEquals(false, checker.isValidCase(case, ""))
        assertEquals(false, checker.isValidCase(case, "camelCaseTest_"))
    }

    @Test
    fun testSnakeCase() {
        val case = CaseConvention.SNAKE_CASE
        assertEquals(true, checker.isValidCase(case, "hello_world"))
        assertEquals(false, checker.isValidCase(case, "HelloWorld_"))
        assertEquals(false, checker.isValidCase(case, "camelCaseTest"))
        assertEquals(true, checker.isValidCase(case, "snake_case_test"))
    }
}
