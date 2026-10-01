package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.get
import ghoti.maedjyuk.app.utilities.set
import kotlin.collections.forEach

class Day15(
    input: String,
) {
    private val grid: List<CharArray>
    private val moves: List<Point2D>

    init {
        val (map, movements) = input.split(System.lineSeparator() + System.lineSeparator())
        grid = map.lines().map(String::toCharArray)
        moves =
            movements.mapNotNull { c ->
                when (c) {
                    '<' -> Point2D.LEFT
                    '^' -> Point2D.UP
                    '>' -> Point2D.RIGHT
                    'v' -> Point2D.DOWN
                    '\r' -> null
                    '\n' -> null
                    else -> throw IllegalArgumentException("Unknown movement $c")
                }
            }
    }

    private fun List<CharArray>.findAll(target: Char): List<Point2D> =
        flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == target) Point2D(x, y) else null
            }
        }.filterNotNull()

    private fun List<CharArray>.push(
        position: Point2D,
        direction: Point2D,
    ): List<Point2D>? {
        val pushes: MutableList<Point2D> = mutableListOf()
        val queue = mutableListOf(position)
        val seen = mutableSetOf<Point2D>()

        while (queue.isNotEmpty()) {
            val thisPosition = queue.removeFirst()
            if (thisPosition !in seen) {
                seen.add(thisPosition)
                if (direction in setOf(Point2D.UP, Point2D.DOWN)) {
                    when (this[thisPosition]) {
                        ']' -> queue.add(thisPosition + Point2D.LEFT)
                        '[' -> queue.add(thisPosition + Point2D.RIGHT)
                    }
                }

                val nextPosition = thisPosition + direction
                when (this[nextPosition]) {
                    '#' -> return null
                    in "[O]" -> queue.add(nextPosition)
                }
                pushes.add(thisPosition)
            }
        }

        return pushes.reversed()
    }

    private fun List<CharArray>.doMoves(): List<CharArray> {
        val start = findAll('@').first()
        var place = start

        moves.forEach { direction ->
            val next = place + direction
            when (this[next]) {
                in "[O]" -> {
                    push(next, direction)
                        ?.let { moves ->
                            moves.forEach { from ->
                                this[from + direction] = this[from]
                                this[from] = '.'
                            }
                            place = next
                        }
                }

                !in "#" -> {
                    place = next
                }
            }
        }

        return this
    }

    private fun Point2D.gps(): Int = x + (100 * y)

    fun solvePart1(): Int =
        grid
            .doMoves()
            .also { println(grid.joinToString(System.lineSeparator(), transform = { it.joinToString("") })) }
            .findAll('O')
            .sumOf { it.gps() }

    private fun CharArray.remap(): CharArray =
        joinToString("") {
            when (it) {
                '#' -> "##"
                'O' -> "[]"
                '.' -> ".."
                '@' -> "@."
                else -> throw IllegalArgumentException("Unrecognized character: $it")
            }
        }.toCharArray()

    fun solvePart2(): Int =
        grid
            .map { it.remap() }
            .doMoves()
            .findAll('[')
            .sumOf { it.gps() }
}
