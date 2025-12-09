package ghoti.maedjyuk.app.aoc2025

import ghoti.maedjyuk.app.utilities.Point3D
import ghoti.maedjyuk.app.utilities.cartesianProduct
import ghoti.maedjyuk.app.utilities.distanceTo
import ghoti.maedjyuk.app.utilities.takeWhileInclusive
import kotlin.collections.emptySet

object Day08 {

    private fun parseInput(input: String): List<Point3D> =
        input.split(System.lineSeparator())
            .map { line ->
                val (x, y, z) = line.split(',').map(String::toInt)
                Point3D(x, y, z)
            }

    private fun List<Point3D>.getDistances(): Map<Double, Pair<Point3D, Point3D>> =
        this.cartesianProduct(this)
            .filter { (first, second) -> first != second }
            .associateBy(
                { (first, second) -> first.distanceTo(second) },
                { (first, second) -> first to second }
            )

    private fun connectCircuits(circuits: Set<Set<Point3D>>, pointA: Point3D, pointB: Point3D): Set<Set<Point3D>> {
        val existingCircuits = circuits.filter { circuit ->
            circuit.contains(pointA) || circuit.contains(pointB)
        }

        return when (existingCircuits.size) {
            1 -> {
                val existingCircuit = existingCircuits.first()
                when {
                    existingCircuit.contains(pointA) ->
                        circuits.minusElement(existingCircuit).plusElement(existingCircuit.plus(pointB))
                    existingCircuit.contains(pointB) ->
                        circuits.minusElement(existingCircuit).plusElement(existingCircuit.plus(pointA))
                    else -> circuits
                }
            }
            2 -> {
                circuits.minus(existingCircuits.toSet())
                    .plusElement(existingCircuits.reduce { acc, ds -> acc.plus(ds) })
            }
            else -> {
                circuits.plusElement(setOf(pointA, pointB))
            }
        }
    }

    private fun Map<Double, Pair<Point3D, Point3D>>.makeCircuits(maxConnections: Int): Set<Set<Point3D>> =
        this.toSortedMap()
            .asSequence()
            .take(maxConnections)
            .map { (_, points) -> points }
            .fold(emptySet()) { circuits, (pointA, pointB) -> connectCircuits(circuits, pointA, pointB) }

    fun getCircuitSizes(input: String, connections: Int): Int =
        parseInput(input)
            .getDistances()
            .makeCircuits(connections)
            .also(::println)
            .map(Set<*>::size)
            .sortedDescending()
            .take(3)
            .reduce { total, next -> total * next }

    fun getCircuitFirstSingleCircuit(input: String): Long {
        val allPoints = parseInput(input)
        val allPointsSize = allPoints.size
        val circuitSequence = allPoints.getDistances()
            .toSortedMap()
            .asSequence()

        val dummy = Point3D(0, 0, 0)

        val firstLastPair = circuitSequence.map { (_, points) -> points }
            .runningFold(emptySet<Set<Point3D>>() to (dummy to dummy)) { (circuits, _), (pointA, pointB) ->
                connectCircuits(circuits, pointA, pointB) to (pointA to pointB)
            }.takeWhileInclusive { (circuits, _) ->
                !(circuits.size == 1 && circuits.first().size == allPointsSize)
            }.last()
            .second

        return firstLastPair.first.x.toLong() * firstLastPair.second.x
    }
}
