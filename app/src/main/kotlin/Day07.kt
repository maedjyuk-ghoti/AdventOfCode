package ghoti.maedjyuk.app

import java.util.stream.Collectors

object Day07 {
    private fun Pair<String, String>.propagateTachyons(): Pair<String, Int> {
        val initialTachyons = first.mapIndexed { index, ch -> index to ch }
            .filter { it.second == '|' }
            .map { it.first }

        val splitters = second.mapIndexed { index, ch -> index to ch }
            .filter { it.second == '^' }
            .map { it.first }

        val splitTachyons = initialTachyons.filter { splitters.contains(it) }

        val finalTachyons = splitTachyons.flatMap { index -> listOf(index - 1, index + 1) } +
                initialTachyons.minus(splitTachyons.toSet())

        return List(first.length) { index ->
            when {
                finalTachyons.contains(index) -> '|'
                splitters.contains(index) -> '^'
                else -> '.'
            }
        }.joinToString(separator = "") to splitTachyons.size
    }

    private fun Pair<String, String>.initialTachyonPropagation(): String {
        val start = first.indexOf('S')

        return second.toMutableList()
            .apply { this[start] = '|' }
            .joinToString(separator = "")
    }

    fun solveTachyonManifold(input: String): Int =
        input.split(System.lineSeparator())
            .let { lines ->
                val (one, two) = lines.take(2)
                val initial = Pair(one, two).initialTachyonPropagation()

                lines.drop(2)
                    .fold(initial to 0) { (prevLine, count), nextLine ->
                        val propResult = Pair(prevLine, nextLine).propagateTachyons()
                        propResult.first to (count + propResult.second)
                    }.second
            }

    private fun String.propagateTachyonsQuantum(weights: Map<Int, Long>): Map<Int, Long> {
        val initialTachyons = weights.entries.map { Pair(it.key, it.value) }

        val splitters = this.mapIndexed { index, ch -> index to ch }
            .filter { it.second == '^' }
            .map { it.first }

        val splitTachyons = initialTachyons.filter { splitters.contains(it.first) }

        val finalTachyons = (splitTachyons.flatMap { (index, weight) -> listOf((index - 1 to weight), (index + 1) to weight) } +
                initialTachyons.minus(splitTachyons.toSet()))
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }

        return finalTachyons
    }

    fun solveTachyonManifoldQuantum(input: String): Long =
        input.split(System.lineSeparator())
            .let { lines ->
                val start = lines.first().indexOf('S')

                lines.drop(1)
                    .fold(mapOf(start to 1L)) { prevWeights, nextLine ->
                        nextLine.propagateTachyonsQuantum(prevWeights)
                    }.values.sum()
            }
}
