package ghoti.maedjyuk.app.aoc2024

class Day11(
    input: String,
) {
    private val stones: List<Long> = input.split(' ').map(String::toLong)

    private fun Long.hasEvenDigits(): Boolean = toString().length % 2 == 0

    private fun Long.split(): List<Long> {
        val lString = toString()

        return listOf(
            lString.take(lString.length / 2).toLong(),
            lString.takeLast(lString.length / 2).toLong(),
        )
    }

    private val cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

    private fun blinkRec(
        stone: Long,
        blinks: Int,
        key: Pair<Long, Int> = stone to blinks,
    ): Long =
        when {
            blinks == 0 -> {
                1L
            }

            key in cache -> {
                cache.getValue(key)
            }

            else -> {
                when {
                    stone == 0L -> blinkRec(1, blinks - 1)
                    stone.hasEvenDigits() -> stone.split().sumOf { blinkRec(it, blinks - 1) }
                    else -> blinkRec(stone * 2024, blinks - 1)
                }.also { result -> cache[key] = result }
            }
        }

    private fun sumBlinks(times: Int): Long = stones.sumOf { blinkRec(it, times) }

    fun solvePart1(): Long = sumBlinks(25)

    fun solvePart2(): Long = sumBlinks(75)
}
