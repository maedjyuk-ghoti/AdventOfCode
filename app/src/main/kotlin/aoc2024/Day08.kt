package ghoti.maedjyuk.app.aoc2024

import ghoti.maedjyuk.app.utilities.Point2D

class Day08(
    input: String,
) {
    val data = input.split(System.lineSeparator())

    val nodes: Collection<List<Point2D>> = parseGrid(data)

    fun parseGrid(input: List<String>): Collection<List<Point2D>> =
        input
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    if (c != '.') c to Point2D(x, y) else null
                }
            }.filterNotNull()
            .groupBy({ it.first }, { it.second })
            .values

    fun Point2D.isOnGrid(): Boolean = y in data.indices && x in data[y].indices

    private fun countAntinodes(worker: (Point2D, Point2D, Point2D) -> Set<Point2D>): Int =
        nodes
            .flatMap { nodeList ->
                nodeList
                    .flatMapIndexed { index, a -> nodeList.drop(index + 1).flatMap { b -> worker(a, b, a - b) } }
                    .filter { it.isOnGrid() }
            }.toSet()
            .size

    private fun antinodes1(
        a: Point2D,
        b: Point2D,
        diff: Point2D,
    ): Set<Point2D> =
        if (a.y > b.y) {
            setOf(a - diff, b + diff)
        } else {
            setOf(a + diff, b - diff)
        }

    private fun antinodes2(
        a: Point2D,
        b: Point2D,
        diff: Point2D,
    ): Set<Point2D> =
        generateSequence(a) { it - diff }.takeWhile { it.isOnGrid() }.toSet() +
            generateSequence(a) { it + diff }.takeWhile { it.isOnGrid() }.toSet()

    fun solvePart1(): Int = countAntinodes(::antinodes1)

    fun solvePart2(): Int = countAntinodes(::antinodes2)
}
