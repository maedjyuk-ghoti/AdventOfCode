package utilities

private fun getInput(fileName: String): String =
    object {}.javaClass
        .getResourceAsStream(fileName)!!
        .bufferedReader()
        .readText()

fun <T> functionTest(
    inputFile: String,
    function: (String) -> T,
    test: (T) -> Unit
) {
    val input = getInput("../$inputFile")
    val actual = function(input)

    test(actual)
}

fun <T> functionTest(
    inputFile: String,
    inputSize: Int,
    function: (String, Int) -> T,
    test: (T) -> Unit
) {
    val input = getInput(inputFile)
    val actual = function(input, inputSize)

    test(actual)
}