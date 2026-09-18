package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D

class Day06(
    input: String,
) {
    private val grid: List<CharArray> = input.split(System.lineSeparator()).map(String::toCharArray)

    private val start: Point2D =
        grid
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    if (c == '^') Point2D(x, y) else null
                }
            }.filterNotNull()
            .first()

    private fun Point2D.turn(): Point2D =
        when (this) {
            Point2D.LEFT -> Point2D.UP
            Point2D.UP -> Point2D.RIGHT
            Point2D.RIGHT -> Point2D.DOWN
            Point2D.DOWN -> Point2D.LEFT
            else -> throw IllegalStateException("Bad direction: $this")
        }

    private operator fun List<CharArray>.get(at: Point2D): Char? = getOrNull(at.y)?.getOrNull(at.x)

    private operator fun List<CharArray>.set(
        at: Point2D,
        c: Char,
    ) {
        this[at.y][at.x] = c
    }

    private fun traverse(): Pair<Set<Point2D>, Boolean> {
        val seen = mutableSetOf<Pair<Point2D, Point2D>>()
        var location = start
        var direction = Point2D.UP

        while (grid[location] != null && (location to direction) !in seen) {
            seen += location to direction
            val next = location + direction

            if (grid[next] == '#') {
                direction = direction.turn()
            } else {
                location = next
            }
        }
        return seen.map { it.first }.toSet() to (grid[location] != null)
    }

    fun solvePart1(): Int = traverse().first.size

    fun solvePart2(): Int =
        traverse()
            .first
            .filterNot { it == start }
            .count { candidate ->
                grid[candidate] = '#'
                traverse().also { grid[candidate] = '.' }.second
            }
}
