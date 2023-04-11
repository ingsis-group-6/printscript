package lexer

import common.token.TokenType
import lexer.factory.TokenTypeManagerFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TokenTypeManagerTest {

    val tokenTpyeManager = TokenTypeManagerFactory.createPrintScriptTokenTypeManager()

    @Test
    fun correctTokenTypeListCreationTest() {
        Assertions.assertEquals(tokenTpyeManager.getTokenTypeList().size, 21)
    }

    @Test
    fun correctTokenTypeListCreationTest2() {
        Assertions.assertEquals(tokenTpyeManager.getTokenTypeList()[0].tokenType, TokenType.ASSIGNATION)
    }

    @Test
    fun correctFunctionWorkingAssignation(){
        Assertions.assertEquals(tokenTpyeManager.getTokenTypeList()[0].validateType("a"), false)
        Assertions.assertEquals(tokenTpyeManager.getTokenTypeList()[0].validateType("="), true)

    }

}