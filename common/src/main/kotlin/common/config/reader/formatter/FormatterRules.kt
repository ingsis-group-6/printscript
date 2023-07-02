package common.config.reader.formatter

import common.config.reader.JsonReader

data class CustomFormatterRules(
    val spaceBeforeColon: Int,
    val spaceAfterColon: Int,
    val spaceBeforeAndAfterAssignationOperator: Int,
    val newlinesBeforePrintln: Int,
    val conditionalBlockIndentation: Int
)

class FormatterRules {
    val newlinesAfterSemicolon: Int = 1
    val spacesBetweenTokens: Int = 1
    val spacesBeforeAndAfterOperators: Int = 1
    val custom: CustomFormatterRules

    constructor(configFileName: String) {
        custom = JsonReader().readJsonFromFile<CustomFormatterRules>(configFileName)
            ?: throw Exception("Config file was not found.")
    }

    constructor(customFormatterRules: CustomFormatterRules) {
        custom = customFormatterRules
    }
}
