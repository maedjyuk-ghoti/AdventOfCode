package ghoti.maedjyuk.app.aoc2025

import ghoti.maedjyuk.app.utilities.addRangeAndCombine
import ghoti.maedjyuk.app.utilities.countL
import kotlin.collections.fold

/**
 * Notes:
 *
 * If you have [Collection]<[LongRange]>, adding or subtracting [LongRange] returns [Collection]<[Any]>
 *  because a [LongRange] is interpreted as [Iterable]<[Long]>.
 *
 * [LongRange] exposes [Iterable.count] which only returns an [Int] and can overflow given a large enough range,
 *  which is absolutely possible given that we're dealing with [Long].
 *
 * [Iterable.count] can also overflow the heap space because it expands the range than iterates over each element.
 *  See [countL] for a workaround.
 */
object Day05 {

    private fun parseInput(input: String): Pair<Set<LongRange>, Set<Long>> {
        // Find the empty line by splitting on 2 line breaks
        val (freshRangesString, ingredientsString) =input.split(System.lineSeparator().plus(System.lineSeparator()))

        val freshRanges = freshRangesString.split(System.lineSeparator())
            .map { rangeString ->
                rangeString.split('-')
                    .map(String::toLong)
                    .let { (low, high) -> LongRange(low, high) }
            }.toSet()

        val ingredients = ingredientsString.split(System.lineSeparator())
            .map(String::toLong)
            .toSet()

        return freshRanges to ingredients
    }

    private fun Pair<Set<LongRange>, Set<Long>>.countFreshIngredients(): Int =
        second.count { ingredient ->
            first.any { range -> range.contains(ingredient) }
        }

    fun getFreshIngredientCount(input: String): Int =
        parseInput(input)
            .countFreshIngredients()

    private fun Pair<Set<LongRange>, Set<Long>>.countPossibleFreshIngredients(): Long =
        first.fold(emptySet<LongRange>()) { ranges, newRange -> ranges.addRangeAndCombine(newRange) }
            .sumOf(LongRange::countL)

    fun getFreshIngredientCount2(input: String): Long =
        parseInput(input)
            .countPossibleFreshIngredients()
}
