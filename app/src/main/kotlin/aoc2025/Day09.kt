package ghoti.maedjyuk.app.aoc2025

import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.Rectangle

object Day09 {
    private fun parseInput(input: String): List<Point2D> =
        input
            .split(System.lineSeparator())
            .map { line ->
                val (x, y) = line.split(',')
                Point2D(x.toInt(), y.toInt())
            }

    fun areaOfLargestRectangle(input: String): Long =
        parseInput(input)
            .let { points ->
                points
                    .flatMapIndexed { index, left -> points.drop(index + 1).map { right -> Rectangle.of(left, right) } }
                    .maxBy(Rectangle::area)
                    .area
            }

    fun areaOfLargestCorrectRectangle(input: String): Long =
        parseInput(input)
            .let { points ->
                // create all lines connecting each corner of the input
                val lines: List<Rectangle> =
                    (points + points.first())
                        .zipWithNext()
                        .map { (left, right) -> Rectangle.of(left, right) }

                points
                    .flatMapIndexed { index, left ->
                        // create rectangles using this point (left) and all subsequent points (right)
                        points
                            .drop(index + 1)
                            .map { right -> Rectangle.of(left, right) }
                    }.filter { rectangle ->
                        // make sure no lines intersect the _inner_ rectangle
                        val inner = rectangle.inner()
                        lines.none { line -> line.overlaps(inner) }
                    }.maxBy(Rectangle::area)
                    .area
            }
}
