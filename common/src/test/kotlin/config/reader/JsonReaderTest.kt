package config.reader

import common.config.reader.JsonReader
import common.config.reader.linter.CaseConvention
import common.config.reader.linter.LinterConfigInput
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class JsonReaderTest {

    @Test
    fun test1() {
        val jr = JsonReader()
        val obj = jr.readJsonFromFile<LinterConfigInput>("/Users/rick/faculty/printscript-v1/common/src/test/resources/json_reader_test.json")
        val expected = LinterConfigInput(CaseConvention.CAMEL_CASE, false)
        Assertions.assertEquals(expected, obj)
    }
}
