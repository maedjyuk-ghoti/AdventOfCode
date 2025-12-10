package ghoti.maedjyuk.app.aoc2025

object Day10 {
    private data class Manual(
        val indicatorLights: String,
        val buttonSchematics: List<Set<Int>>,
        val joltageRequirements: Set<Int>
    )

    private fun String.removeBraces(): String =
        this.substringAfter('(')
            .substringAfter('{')
            .substringAfter('[')
            .substringBeforeLast(')')
            .substringBeforeLast('}')
            .substringBeforeLast(']')

    private fun parseInput(input: String): List<Manual> =
        input.lines()
            .map { line ->
                val instructions = line.split(' ')

                val indicatorLights = instructions.first()
                    .removeBraces()

                val buttonSchematics = instructions.drop(1)
                    .take(instructions.size - 2)
                    .map {
                        it.removeBraces()
                            .split(',')
                            .map(String::toInt)
                            .toSet()
                    }

                val joltageRequirements = instructions.last()
                    .removeBraces()
                    .split(',')
                    .map(String::toInt)
                    .toSet()

                Manual(indicatorLights, buttonSchematics, joltageRequirements)
            }

    private fun getIndicatorLightsFor(lightCount: Int, buttonPresses: Map<Int, Boolean>): String =
        buttonPresses.entries
            .fold(MutableList(lightCount) {'.'}) { acc, (index, status) ->
                acc[index] = if (status) '#' else '.'
                acc
            }.joinToString(separator = "")

    private tailrec fun attemptSolve(
        targetIndicatorLights: String,
        availableButtonPresses: List<Set<Int>>,
        currentButtonPresses: List<List<Int>>
    ): List<Int> {
        val solution = currentButtonPresses.find { buttonPresses ->
            val currentButtonStatuses = buttonPresses.fold(mutableMapOf<Int, Boolean>()) { acc, index ->
                availableButtonPresses[index].forEach { indicatorIndex ->
                    acc[indicatorIndex] = !acc.getOrDefault(indicatorIndex, false)
                }
                acc
            }
            getIndicatorLightsFor(targetIndicatorLights.length, currentButtonStatuses) == targetIndicatorLights
        }
        if (solution != null) return solution

        // else
        val nextButtonPresses = if (currentButtonPresses.isEmpty()) {
                availableButtonPresses.indices.map { listOf(it) }
            } else {
                availableButtonPresses.indices.flatMap { availableButton ->
                    currentButtonPresses.map { buttonPresses ->
                        buttonPresses.plusElement(availableButton)
                    }.filter { it.distinct().size == it.size } // remove cases where a button is clicked twice (useless toggle)
            }
        }

        return attemptSolve(targetIndicatorLights, availableButtonPresses, nextButtonPresses)
    }

    private fun getSolves(manual: Manual): List<Int> =
        attemptSolve(manual.indicatorLights, manual.buttonSchematics, emptyList())

    fun initializeMachines(input: String): Int =
        parseInput(input)
            .map(::getSolves)
            .sumOf(List<*>::size)
}