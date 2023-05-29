package common.config.reader.formatter

import common.config.reader.JsonReader

data class CustomizableFormatterRules(
    val spaceBeforeColon: Int,
    val spaceAfterColon: Int,
    val spaceBeforeAndAfterAssignationOperator: Int,
    val newlinesBeforePrintln: Int,
    val conditionalBlockIndentation: Int
)

class FormatterRules(configFileName: String) {
    val newlinesAfterSemicolon: Int = 1
    val spacesBetweenTokens: Int = 1
    val spacesBeforeAndAfterOperators: Int = 1
    val custom = JsonReader().readJsonFromFile<CustomizableFormatterRules>(configFileName) ?: throw Exception("Config file was not found.")
}
