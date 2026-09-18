package ghoti.maedjyuk.app.aoc2024

class Day06(
    input: String,
) {
    val map: Map<Pair<Int, Int>, Char> =
        input
            .split(System.lineSeparator())
            .map(String::toList)
            .flatMapIndexed { y, row -> row.mapIndexed { x, c -> Pair(x, y) to c } }
            .toMap()

    val bounds: Pair<IntRange, IntRange> =
        input
            .split(System.lineSeparator())
            .let { lines -> lines.indices to lines.first().indices }

    private companion object {
        val LEFT = -1 to 0
        val UP = 0 to -1
        val RIGHT = 1 to 0
        val DOWN = 0 to 1

        val GUARD_SYMBOLS: List<Char> =
            listOf('<', '^', '>', 'v')

        fun nextDirection(direction: Pair<Int, Int>): Pair<Int, Int> =
            when (direction) {
                LEFT -> UP
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                else -> 0 to 0
            }
    }

    private fun Pair<Int, Int>.inBounds(): Boolean = first in bounds.first && second in bounds.second

    private fun Map<Pair<Int, Int>, Char>.findGuard(): Pair<Int, Int> =
        entries
            .find { (_, v) -> GUARD_SYMBOLS.contains(v) }
            ?.key ?: (-1 to -1)

    private tailrec fun getUniqueGuardPositions(
        map: Map<Pair<Int, Int>, Char>,
        direction: Pair<Int, Int>,
        memo: Set<Pair<Int, Int>>,
    ): Set<Pair<Int, Int>> {
        val guardCoords = map.findGuard()
        if (!guardCoords.inBounds()) return memo

        val nextStep = (guardCoords.first + direction.first) to (guardCoords.second + direction.second)

        return if (map[nextStep] == '#') {
            getUniqueGuardPositions(map, nextDirection(direction), memo)
        } else {
            getUniqueGuardPositions(
                map
                    .toMutableMap()
                    .apply {
                        this[guardCoords] = 'X'
                        this[nextStep] = '^'
                    },
                direction,
                memo + guardCoords,
            )
        }
    }

    private fun Map<Pair<Int, Int>, Char>.getUniqueGuardPositions(): Set<Pair<Int, Int>> = getUniqueGuardPositions(this, UP, emptySet())

    private tailrec fun getUniqueGuardPositions2(
        map: Map<Pair<Int, Int>, Char>,
        guard: Pair<Int, Int>,
        direction: Pair<Int, Int>,
        memo: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    ): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        if (!guard.inBounds()) return memo

        val nextStep = (guard.first + direction.first) to (guard.second + direction.second)

        return if (map[nextStep] == '#') {
            getUniqueGuardPositions2(map, guard, nextDirection(direction), memo)
        } else {
            getUniqueGuardPositions2(
                map,
                nextStep,
                direction,
                memo + (guard to direction),
            )
        }
    }

    private fun Map<Pair<Int, Int>, Char>.getUniqueGuardPositions2(): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> =
        getUniqueGuardPositions2(this, findGuard(), UP, emptyList())

    private tailrec fun isLoop(
        map: Map<Pair<Int, Int>, Char>,
        direction: Pair<Int, Int>,
        memo: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    ): Boolean {
        val guardCoords = map.findGuard()
        if (!guardCoords.inBounds()) return false
        if (memo.contains(guardCoords to direction)) return true

        val nextStep = (guardCoords.first + direction.first) to (guardCoords.second + direction.second)

        return if (map[nextStep] == '#') {
            isLoop(map, nextDirection(direction), memo)
        } else {
            isLoop(
                map
                    .toMutableMap()
                    .apply {
                        this[guardCoords] = 'X'
                        this[nextStep] = '^'
                    },
                direction,
                memo + (guardCoords to direction),
            )
        }
    }

    private fun findLoops(map: Map<Pair<Int, Int>, Char>): Int {
        val uniquePositions = map.getUniqueGuardPositions2()
        val guardStart = map.findGuard()
        return uniquePositions.indices
            .reversed()
            .take(uniquePositions.size - 1)
            .map { index ->
                // starting from index
                // put obstacle at index, user at index - 1
                val newObstacleCoords = uniquePositions[index].first
                val newGuardStart = uniquePositions[index - 1].first
                val newDirection = uniquePositions[index - 1].second

                val newMap =
                    map.toMutableMap().apply {
                        this[guardStart] = '.'
                        this[newObstacleCoords] = '#'
                        this[newGuardStart] = '^'
                    }
                // take unique positions up to index
                // run forward and check for a loop
                newObstacleCoords to isLoop(newMap, newDirection, uniquePositions.subList(0, index - 1))
            }.distinctBy { it.first }
            .count { it.second }
    }

    fun solvePart1(): Int =
        map
            .getUniqueGuardPositions()
            .count()

    fun solvePart2(): Int = findLoops(map)
}
