package lexer.token

data class Token(
    val order_id: Int,
    val tokenType: TokenType,
    val value: String,
    val row: Int,
    // val col: Int  --> En el momento de crear los tokens en el lexer, fijarse de ir sumando las lengths de las palabras

    /*
     var currentCol = 0;

        for (word in stringWords){
            for ((tokenTypeChecker, tokenGeneratorFunction) in tokenizerMap){
                if(tokenTypeChecker.validateType(word)){
                    tokenList.add(tokenGeneratorFunction(tokenTypeChecker.tokenType, word, row, currentCol))
                    currentCol += word.length
                }
            }
        }
     */
)
