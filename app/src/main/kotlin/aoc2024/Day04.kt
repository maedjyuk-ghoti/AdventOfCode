package ghoti.maedjyuk.app.aoc2024

class Day04(
    input: String,
) {
    val data: List<List<Char>> = input.uppercase().split(System.lineSeparator()).map(String::toList)

    private companion object {
        val ALL_DIRECTIONS =
            listOf(
                -1 to -1, // up & left
                -1 to 0, // up
                -1 to 1, // up & right
                0 to -1, // left
                0 to 1, // right
                1 to -1, // down & left
                1 to 0, // down
                1 to 1, // down & right
            )

        val CORNERS =
            listOf(
                -1 to -1, // up & left
                -1 to 1, // up & right
                1 to 1, // down & right
                1 to -1, // down & left
            )
    }

    private fun List<List<Char>>.get(
        x: Int,
        y: Int,
    ): Char = if (y in indices && x in this[y].indices) this[y][x] else ' '

    private tailrec fun vectorFind(
        target: String,
        x: Int,
        y: Int,
        vector: Pair<Int, Int>,
    ): Boolean =
        when {
            target.isEmpty() -> true
            target.first() != data.get(x, y) -> false
            else -> vectorFind(target.substring(1), x + vector.first, y + vector.second, vector)
        }

    fun solvePart1(): Int =
        data
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    if (c == 'X') {
                        ALL_DIRECTIONS.count { vector -> vectorFind("XMAS", x, y, vector) }
                    } else {
                        0
                    }
                }
            }.sum()

    fun solvePart2(): Int =
        data
            .flatMapIndexed { y, row ->
                row.mapIndexed { x, c ->
                    if (c == 'A') {
                        val cornerCheck =
                            CORNERS
                                .map { (dx, dy) -> data.get(x + dx, y + dy) }
                                .joinToString("")
                        cornerCheck in setOf("MMSS", "MSSM", "SSMM", "SMMS")
                    } else {
                        false
                    }
                }
            }.count { it }
}
