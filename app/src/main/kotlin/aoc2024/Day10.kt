package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.get

class Day10(
    input: String,
) {
    private val data: List<List<Int>> = input.split(System.lineSeparator()).map { line -> line.map(Char::digitToInt) }

    private fun getTrailsFrom(start: Point2D): Set<List<Point2D>> {
        if (data[start] == 9) return setOf(listOf(start))

        return Point2D.CARDINAL_DIRECTIONS
            .map { direction -> start + direction }
            .filter { nextStep -> data[nextStep] != null && (data[nextStep]!! - data[start]!!) == 1 }
            .flatMap { nextStep -> getTrailsFrom(nextStep).map { path -> path.plus(start) } }
            .toSet()
    }

    fun solvePart1(): Int =
        data
            .flatMapIndexed { y, row -> row.mapIndexed { x, height -> if (height == 0) Point2D(x, y) else null } }
            .filterNotNull()
            .map { trailhead -> getTrailsFrom(trailhead) }
            .sumOf { trails -> trails.distinctBy { trail -> trail.first() }.count() }

    fun solvePart2(): Int =
        data
            .flatMapIndexed { y, row -> row.mapIndexed { x, height -> if (height == 0) Point2D(x, y) else null } }
            .filterNotNull()
            .map { trailhead -> getTrailsFrom(trailhead) }
            .sumOf { trails -> trails.count() }
}
