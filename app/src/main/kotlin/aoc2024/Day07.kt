package ghoti.maedjyuk.app.aoc2024

class Day07(
    input: String,
) {
    val equations: List<List<Long>> =
        input
            .split(System.lineSeparator())
            .map { line -> line.split("""\D+""".toRegex()).map(String::toLong) }

    val operators: List<(Long, Long) -> Long> =
        listOf(
            { a, b -> a + b },
            { a, b -> a * b },
            { a, b -> "$a$b".toLong() },
        )

    private fun solve(validOperators: List<(Long, Long) -> Long>): Long =
        equations
            .filter { hasSolution(validOperators, it[0], it[1], it.subList(2, it.size)) }
            .sumOf { it.first() }

    private fun hasSolution(
        operators: List<(Long, Long) -> Long>,
        target: Long,
        sum: Long,
        remaining: List<Long>,
    ): Boolean =
        when {
            remaining.isEmpty() -> {
                target == sum
            }

            (sum > target) -> {
                false
            }

            else -> {
                operators.any { operator ->
                    hasSolution(
                        operators,
                        target,
                        operator(sum, remaining[0]),
                        remaining.subList(1, remaining.size),
                    )
                }
            }
        }

    fun solvePart1(): Long = solve(operators.take(2))

    fun solvePart2(): Long = solve(operators)
}
