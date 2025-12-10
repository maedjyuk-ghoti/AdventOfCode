package ghoti.maedjyuk.app.aoc2025

import ghoti.maedjyuk.app.utilities.Point2D
import ghoti.maedjyuk.app.utilities.cartesianProduct
import kotlin.math.absoluteValue

object Day09 {

    private fun parseInput(input: String): List<Point2D> =
        input.split(System.lineSeparator())
            .map { line ->
                val (x, y) = line.split(',')
                Point2D(x.toInt(), y.toInt())
            }

    private fun getAreaOfRectangle(corner1: Point2D, corner2: Point2D): Long =
        ((corner1.x - corner2.x).absoluteValue + 1).toLong() *
                ((corner1.y - corner2.y).absoluteValue + 1)

    fun areaOfLargestRectangle(input: String): Long =
        parseInput(input)
            .let { points ->
                points.cartesianProduct(points)
                    .maxOf { (point1, point2) -> getAreaOfRectangle(point1, point2) }
            }
}
