package common.providers.token

import common.token.Token
import java.util.*

interface TokenProvider {
    fun getToken(): Optional<Token>
}
