package ghoti.maedjyuk.app.aoc2024

import kotlin.math.max
import kotlin.math.min

class Day01(
    input: String,
) {
    private val lists: Pair<List<Int>, List<Int>> =
        input
            .split(System.lineSeparator())
            .map { line -> line.split("\\s+".toRegex()).map { it.toInt() } }
            .let { lines -> lines.map { line -> line.first() } to lines.map { line -> line.last() } }

    private val myList = lists.first
    private val historiansList = lists.second

    fun solvePart1(): Int =
        myList
            .sorted()
            .zip(historiansList.sorted()) { mine, historians -> max(mine, historians) - min(mine, historians) }
            .sum()

    fun solvePart2(): Int {
        val occurrenceMap =
            historiansList
                .groupBy { it }
                .mapValues { (_, value) -> value.count() }

        return myList.sumOf { number -> number * occurrenceMap.getOrDefault(number, 0) }
    }
}
