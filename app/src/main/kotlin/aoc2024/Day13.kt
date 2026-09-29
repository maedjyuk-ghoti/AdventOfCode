package ghoti.maedjyuk.app.aoc2024

class Day13(
    input: String,
) {
    private data class Machine(
        val aX: Long,
        val aY: Long,
        val bX: Long,
        val bY: Long,
        val prizeX: Long,
        val prizeY: Long,
    ) {
        companion object {
            fun of(input: List<String>): Machine =
                Machine(
                    input[0].substringAfter('+').substringBefore(',').toLong(),
                    input[0].substringAfterLast('+').toLong(),
                    input[1].substringAfter('+').substringBefore(',').toLong(),
                    input[1].substringAfterLast('+').toLong(),
                    input[2].substringAfter('=').substringBefore(',').toLong(),
                    input[2].substringAfterLast('=').toLong(),
                )
        }

        fun pressButtons(): Long {
            val det = (aX * bY) - (aY * bX)
            val a = (prizeX * bY - prizeY * bX) / det
            val b = (aX * prizeY - aY * prizeX) / det
            return if (aX * a + bX * b == prizeX && aY * a + bY * b == prizeY) {
                a * 3 + b
            } else {
                0
            }
        }
    }

    private val machines: List<Machine> = input.lines().chunked(4).map { Machine.of(it) }

    fun solvePart1(): Long =
        machines
            .sumOf { machine -> machine.pressButtons() }

    private val part2PrizeFix: Long = 10000000000000

    fun solvePart2(): Long =
        machines
            .sumOf { machine ->
                machine.copy(prizeX = machine.prizeX + part2PrizeFix, prizeY = machine.prizeY + part2PrizeFix).pressButtons()
            }
}
