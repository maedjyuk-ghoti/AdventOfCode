package ghoti.maedjyuk.app

import kotlin.collections.component1
import kotlin.collections.set

object Day06 {
    private val spacesRegex = Regex("\\s+")
    private enum class Operation {
        Add {
            override fun operate(list: List<Int>): Long =
                list.fold(0L) { acc, value -> acc + value }
        },
        Multiply {
            override fun operate(list: List<Int>): Long =
                list.fold(1L) { acc, value -> acc * value }
        };
        abstract fun operate(list: List<Int>): Long
    }

    private fun String.parseOperations(): List<Operation> =
        this.split(spacesRegex)
            .map { string ->
                when {
                    string.contains("+") -> Operation.Add
                    string.contains("*") -> Operation.Multiply
                    else -> Operation.Add
                }
            }

    fun List<String>.parseValues(): List<List<Int>> =
        map { line -> line.split(spacesRegex).filter(String::isNotEmpty) }
            .flatMap { line -> line.mapIndexed { index, string -> index to string.toInt() } }
            .groupBy(Pair<Int, Int>::first, Pair<Int, Int>::second)
            .entries
            .sortedBy { (index, _) -> index }
            .map { (_, value) -> value }

    fun getGrandTotal(input: String): Long =
        input.split(System.lineSeparator())
            .let { lines ->
                val indexValueMap = lines.take(lines.size - 1)
                    .parseValues()

                lines.last()
                    .parseOperations()
                    .mapIndexed { index, operation ->
                        operation.operate(indexValueMap[index])
                    }.sum()
            }

    private fun List<String>.parseValues2(): List<List<Int>> =
        map(String::toList)
            .rotateCounterClockwise(' ')
            .map { line -> line.joinToString(separator = "") }
            .fold(Pair(mutableMapOf<Int, List<Int>>(), 0)) { (acc, index), value ->
                if (value.isBlank()) {
                    acc to (index + 1)
                } else {
                    acc[index] = acc.getOrDefault(index, emptyList()).plus(value.trim().toInt())
                    acc to index
                }
            }.first
            .entries
            .sortedBy { (index, _) -> index }
            .map { (_, value) -> value }

    fun getGrandTotal2(input: String): Long =
        input.split(System.lineSeparator())
            .let { lines ->
                val indexValueMap = lines.take(lines.size - 1)
                    .parseValues2()

                lines.last()
                    .parseOperations()
                    .reversed()
                    .mapIndexed { index, operation ->
                        operation.operate(indexValueMap[index])
                    }.sum()
            }
}
