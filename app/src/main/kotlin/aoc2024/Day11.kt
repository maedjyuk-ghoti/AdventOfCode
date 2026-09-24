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

    private fun Long.blink(): List<Long> =
        when {
            this == 0L -> listOf(1)
            this.hasEvenDigits() -> this.split()
            else -> listOf(this * 2024)
        }

    private fun Map<Long, Long>.blink(): Map<Long, Long> =
        flatMap { (value, count) -> value.blink().map { it to count } }
            .groupBy(Pair<Long, Long>::first, Pair<Long, Long>::second)
            .mapValues { it.value.sum() }

    private fun Map<Long, Long>.blink(times: Int): Map<Long, Long> =
        generateSequence(this) { it.blink() }
            .drop(1)
            .take(times)
            .last()

    fun solvePart1(): Long =
        stones
            .associateWith { 1L }
            .blink(25)
            .values
            .sum()

    fun solvePart2(): Long =
        stones
            .associateWith { 1L }
            .blink(75)
            .values
            .sum()
}
