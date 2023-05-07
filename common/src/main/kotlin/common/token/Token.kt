package common.token

data class Token(
    var order_id: Int,
    val tokenType: TokenType,
    val value: String,
    val row: Int,
    val col: Int
)
