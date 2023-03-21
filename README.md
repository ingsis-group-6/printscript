## Para correr PrintScript

Primero pasar la App a un jar

```kotlinc App.kt -include-runtime -d app.jar```

Despues, correrlo agregando los argumentos necesarios

```java -jar app.jar [args]```

# Pasos para el TP

## Lexer

1. El lexer va a recibir archivo, el cual lo va a descomponer el lineas, y al llegar a x lineas, va a cortar y mandarlo como
   input stream
2. generar con todos estos input streams una lista de tokens
3. enviar esta lista al parser

## Parser
1. recibe una lista de tokens del lexer con un tipo y un value
2. crea un arbol con todos los tokens y su valor

## Interpreter

### Cosas extra: formatter n shit 