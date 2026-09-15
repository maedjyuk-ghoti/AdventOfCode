package ghoti.maedjyuk.app.aoc2024

import kotlin.collections.windowed
import kotlin.math.absoluteValue

class Day02(
    input: String,
) {
    val reports: List<List<Int>> =
        input
            .split(System.lineSeparator())
            .map { line -> line.split("\\s+".toRegex()).map(String::toInt) }

    private fun List<Int>.isSafe(): Boolean {
        val changes = windowed(2).map { (prev, next) -> prev - next }

        return (changes.all { it > 0 } || changes.all { it < 0 }) &&
            changes.all { it.absoluteValue in 1..3 }
    }

    /**
     * Check if list would pass if one item was removed
     */
    private fun List<Int>.isSafeWithDamper(): Boolean =
        indices
            .map { index -> take(index) + drop(index + 1) }
            .any { it.isSafe() }

    fun solvePart1(): Int =
        reports
            .map { report -> report.isSafe() }
            .count { it }

    fun solvePart2(): Int =
        reports
            .map { report -> report.isSafe() || report.isSafeWithDamper() }
            .count { it }
}
