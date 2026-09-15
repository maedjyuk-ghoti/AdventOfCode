package ghoti.maedjyuk.app.aoc2025

import kotlin.math.pow
import kotlin.text.toInt

private infix fun Int.pow(x: Int): Int = this.toDouble().pow(x.toDouble()).toInt()

fun <T> List<T>.combinations(): List<Set<T>> =
    if (isEmpty()) {
        listOf(emptySet())
    } else {
        drop(1)
            .combinations()
            .let { powerSetRest -> powerSetRest + powerSetRest.map { it + first() } }
    }

// Check if each item in A is <= the item at the corresponding index in B
fun leqV(
    a: List<Int>,
    b: List<Int>,
) = a.zip(b).all { (x, y) -> x <= y }

fun addV(
    a: List<Int>,
    b: List<Int>,
) = a.zip(b) { x, y -> x + y }

/**
 * The basic idea is to represent things in binary, such that each '#' is a 1 and each '.' is a 0
 * Then each button press turns into an xor between the current status and the alterations of button
 */
object Day10 {
    private data class Schematic(
        val target: Int,
        val buttons: List<Int>,
        val joltages: List<Int>,
    ) {
        companion object {
            fun of(input: String): Schematic {
                val objs = input.split(" ")

                return Schematic(
                    target =
                        objs
                            .first() // first element is the target light schematic
                            .drop(1) // drop the open brace
                            .dropLast(1) // drop the close brace
                            .reversed() // reverse so that the list starts at the left, like binary
                            .fold(0) { acc, character -> (acc shl 1) or if (character == '#') 1 else 0 },
                    // translate to binary
                    buttons =
                        objs
                            .drop(1) // drop the light schematic
                            .dropLast(1) // drop the joltages
                            .map { buttonCombos ->
                                buttonCombos
                                    .drop(1) // drop the open brace
                                    .dropLast(1) // drop the close brace
                                    .split(',') // take each light altered
                                    .fold(0) { acc, next -> acc or (2 pow next.toInt()) } // translate to binary
                            },
                    joltages =
                        objs
                            .last()
                            .substringAfter('{')
                            .substringBefore('}')
                            .split(',')
                            .map { it.toInt() },
                )
            }
        }
    }

    private fun solvePart1(schematic: Schematic): Int =
        schematic.buttons
            .combinations()
            .sortedBy { it.size }
            .first { set -> set.fold(0) { runningLightsStatus, button -> runningLightsStatus xor button } == schematic.target }
            .size

    fun solvePart1(input: String): Int =
        input
            .split("\n")
            .map(Schematic::of)
            .sumOf { schematic -> solvePart1(schematic) }
}

// https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
object Day10Part2 {
    private data class Schematic2(
        val indicators: Int,
        val buttons: List<List<Int>>,
        val joltages: List<Int>,
    ) {
        companion object {
            fun of(input: String): Schematic2 {
                val objs = input.split(" ")

                return Schematic2(
                    indicators =
                        objs
                            .first() // first element is the target light schematic
                            .drop(1) // drop the open brace
                            .dropLast(1) // drop the close brace
                            .reversed() // reverse so that the list starts at the left, like binary
                            .fold(0) { acc, character -> (acc shl 1) or if (character == '#') 1 else 0 },
                    // translate to binary
                    buttons =
                        objs
                            .drop(1) // drop the indicators
                            .dropLast(1) // drop the joltages
                            .map { buttonCombos ->
                                buttonCombos
                                    .drop(1) // drop the open brace
                                    .dropLast(1) // drop the close brace
                                    .split(',') // take each light altered
                                    .map { it.toInt() }
                            },
                    joltages =
                        objs
                            .last()
                            .substringAfter('{')
                            .substringBefore('}')
                            .split(',')
                            .map { it.toInt() },
                )
            }
        }
    }

    private fun solve(
        goal: List<Int>,
        patterns: Map<List<Int>, List<Pair<List<Int>, Int>>>,
        memo: MutableMap<List<Int>, Int?>,
    ): Int? =
        if (goal.all { it == 0 }) {
            0
        } else if (memo.containsKey(goal)) {
            memo[goal]
        } else {
            val parity = goal.map { it % 2 }
            val candidates = patterns[parity]?.filter { (pVec, _) -> leqV(pVec, goal) }

            val best =
                candidates
                    ?.mapNotNull { (p, cost) ->
                        val nextGoal = goal.zip(p) { x, y -> (x - y) / 2 }
                        solve(nextGoal, patterns, memo)?.let { cost + 2 * it }
                    }?.minOrNull()
            memo[goal] = best
            best
        }

    private fun solvePart2(schematic: Schematic2): Int {
        val numJoltages = schematic.joltages.size
        val coeffs = schematic.buttons.map { indexes -> List(numJoltages) { idx -> if (idx in indexes) 1 else 0 } }
        val patterns =
            coeffs.indices
                .fold(listOf(emptyList<Int>())) { runningPatterns, coeffIndex -> runningPatterns + runningPatterns.map { it + coeffIndex } }
                .map { indexes ->
                    val vec = indexes.fold(List(numJoltages) { 0 }) { acc, i -> addV(acc, coeffs[i]) }

                    val parity = vec.map { it % 2 }

                    parity to (vec to indexes.size)
                }.groupBy { it.first }
                .mapValues { (_, pairs) -> pairs.map { it.second } }

        val memo = mutableMapOf<List<Int>, Int?>()

        return solve(schematic.joltages, patterns, memo) ?: 0
    }

    fun solvePart2(input: String): Int =
        input
            .split("\n")
            .map(Schematic2::of)
            .sumOf { solvePart2(it) }
}
