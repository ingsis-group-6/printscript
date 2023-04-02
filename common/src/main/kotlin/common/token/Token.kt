package common.token

data class Token(
    val order_id: Int,
    val tokenType: TokenType,
    val value: String,
    val row: Int
)
