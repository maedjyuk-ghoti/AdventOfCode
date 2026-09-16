package ghoti.maedjyuk.app.aoc2024

class Day03(
    input: String,
) {
    private val memory: String = input
    private val mulRegex: Regex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
    private val inactiveZoneRegex: Regex = Regex("""don't\(\).*?(?:do\(\)|$)""")

    private fun String.getMulValues(): List<Pair<Int, Int>> =
        mulRegex
            .findAll(this)
            .map { match -> (match.groups[1]?.value?.toInt() ?: 0) to (match.groups[2]?.value?.toInt() ?: 0) }
            .toList()

    private fun String.getActiveMulValues(): List<Pair<Int, Int>> =
        replace(System.lineSeparator(), " ")
            .split(inactiveZoneRegex)
            .flatMap { active -> active.getMulValues() }
            .toList()

    fun solvePart1(): Int =
        memory
            .getMulValues()
            .sumOf { (x, y) -> x * y }

    fun solvePart2(): Int =
        memory
            .getActiveMulValues()
            .sumOf { (x, y) -> x * y }
}
