package ghoti.maedjyuk.app.aoc2025

import ghoti.maedjyuk.app.utilities.cartesianProduct

object Day04 {
    private fun parse(input: String): List<String> =
        input.split(System.lineSeparator())

    private fun List<String>.isOutOfBounds(coordinates: Pair<Int, Int>): Boolean =
        coordinates.first < 0 ||
                coordinates.second < 0 ||
                coordinates.first >= this.size ||
                coordinates.second >= this.size

    private fun Int.isLessThan(other: Int): Boolean =
        this < other

    private fun List<String>.checkAround(x: Int, y: Int): Boolean =
        listOf(x - 1, x, x + 1)
            .cartesianProduct(listOf(y - 1, y, y + 1))
            .filter { it != Pair(x, y) }
            .filter { this.isOutOfBounds(it).not() }
            .count { this[it.second][it.first] == '@' }
            .isLessThan(4)

    private fun List<String>.getAccessibleRollsOfPaper(): List<Pair<Int, Int>> =
        this.flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, position ->
                if (position == '@' &&
                    this.checkAround(colIndex, rowIndex)) colIndex to rowIndex
                else null
            }
        }

    private fun List<String>.countAccessibleRollsOfPaper(): Int =
        this.getAccessibleRollsOfPaper()
            .count()

    fun getRollsOfPaperCount(input: String): Int =
        parse(input)
            .countAccessibleRollsOfPaper()

    private fun List<String>.countAccessibleRollsOfPaperComplete(): Int {
        val accessibleRollsOfPaper = this.getAccessibleRollsOfPaper()
        if (accessibleRollsOfPaper.isEmpty()) return 0

        return this.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, value ->
                if (accessibleRollsOfPaper.contains(colIndex to rowIndex)) '.'
                else value
            }.joinToString(separator = "")
        }.countAccessibleRollsOfPaperComplete() + accessibleRollsOfPaper.size
    }

    fun getRollsOfPaperCount2(input: String): Int =
        parse(input)
            .countAccessibleRollsOfPaperComplete()
}