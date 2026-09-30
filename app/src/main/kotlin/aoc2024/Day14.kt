package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D
import kotlin.math.abs

class Day14(
    input: String,
) {
    private data class Robot(
        val position: Point2D,
        val velocity: Point2D,
    ) {
        companion object {
            fun of(input: String): Robot =
                Robot(
                    Point2D(
                        input.substringAfter("p=").substringBefore(",").toInt(),
                        input
                            .substringAfter("p=")
                            .substringAfter(",")
                            .substringBefore(" ")
                            .toInt(),
                    ),
                    Point2D(
                        input.substringAfter("v=").substringBefore(",").toInt(),
                        input.substringAfter("v=").substringAfter(",").toInt(),
                    ),
                )
        }

        fun advance(
            gridX: Int,
            gridY: Int,
        ): Robot {
            val advanced = position + velocity

            val x =
                if (advanced.x in 0 until gridX) {
                    advanced.x
                } else if (advanced.x > gridX) {
                    advanced.x % gridX
                } else {
                    gridX - abs(advanced.x)
                }

            val y =
                if (advanced.y in 0 until gridY) {
                    advanced.y
                } else if (advanced.y > gridY) {
                    advanced.y % gridY
                } else {
                    gridY - abs(advanced.y)
                }

            return Robot(Point2D(x, y), velocity)
        }
    }

    private val robots: List<Robot> = input.lines().map(Robot::of)

    private fun List<Robot>.advance(
        gridX: Int,
        gridY: Int,
    ): List<Robot> = map { robot -> robot.advance(gridX, gridY) }

    private fun List<Robot>.sortQuadrants(
        gridX: Int,
        gridY: Int,
    ): Map<Int, Int> {
        val quadrants =
            listOf(
                Pair(
                    (0 until (gridX / 2)),
                    (0 until (gridY / 2)),
                ),
                Pair(
                    ((gridX / 2) + 1 until gridX),
                    (0 until (gridY / 2)),
                ),
                Pair(
                    (0 until (gridX / 2)),
                    ((gridY / 2) + 1 until gridY),
                ),
                Pair(
                    ((gridX / 2) + 1 until gridX),
                    ((gridY / 2) + 1 until gridY),
                ),
            )

        return groupBy { robot -> quadrants.indexOfFirst { robot.position.x in it.first && robot.position.y in it.second } }
            .filterKeys { key -> key >= 0 }
            .mapValues { (_, robots) -> robots.count() }
    }

    fun solvePart1(
        gridX: Int,
        gridY: Int,
    ): Int =
        generateSequence(robots) { it.advance(gridX, gridY) }
            .drop(1)
            .take(100)
            .last()
            .sortQuadrants(gridX, gridY)
            .values
            .reduce { acc, count -> acc * count }

    fun solvePart2(): Int {
        var moves = 0
        var robotsThisTurn = robots
        do {
            moves++
            robotsThisTurn = robotsThisTurn.map { it.advance(101, 103) }
        } while (robotsThisTurn.distinctBy { it.position }.size != robotsThisTurn.size)
        return moves
    }
}
