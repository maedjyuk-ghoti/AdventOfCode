package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.get

class Day12(
    input: String,
) {
    private val garden: List<List<Char>> = input.lines().map { line -> line.map { it } }

    data class Region(
        val target: Char,
        val area: Int,
        val perimeter: Int,
        val sides: Int,
    )

    private fun findRegions(): List<Region> {
        val seen: MutableSet<Point2D> = mutableSetOf()

        return garden
            .flatMapIndexed { y, row ->
                row.mapIndexedNotNull { x, _ ->
                    val place = Point2D(x, y)
                    if (place !in seen) findRegion(place, seen) else null
                }
            }
    }

    private fun Point2D.countCorners(): Int =
        listOf(Point2D.UP, Point2D.RIGHT, Point2D.DOWN, Point2D.LEFT, Point2D.UP)
            .zipWithNext()
            .map { (first, second) ->
                listOf(
                    garden[this],
                    garden[this + first],
                    garden[this + second],
                    garden[this + first + second],
                )
            }.count { (target, side1, side2, corner) ->
                (target != side1 && target != side2) ||
                    (side1 == target && side2 == target && corner != target)
            }

    private fun findRegion(
        start: Point2D,
        seen: MutableSet<Point2D> = mutableSetOf(),
    ): Region {
        val target: Char = garden[start]!!
        val queue = mutableListOf(start)
        var area = 0
        var perimeter = 0
        var corners = 0

        while (queue.isNotEmpty()) {
            val place = queue.removeFirst()
            if (garden[place] == target && place !in seen) {
                seen += place
                area++
                val neighbors = place.cardinalNeighbors()
                queue.addAll(neighbors)
                perimeter += neighbors.count { garden[it] != target }
                corners += place.countCorners()
            }
        }

        return Region(target, area, perimeter, corners)
    }

    fun solvePart1(): Int =
        findRegions()
            .sumOf { region -> region.area * region.perimeter }

    fun solvePart2(): Int =
        findRegions()
            .sumOf { region -> region.area * region.sides }
}
