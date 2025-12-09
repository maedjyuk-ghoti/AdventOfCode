package ghoti.maedjyuk.app

import kotlin.math.max
import kotlin.math.min

/**
 * Use this in place of [Iterable.count] because a [LongRange] has the potential to overflow an [Int].
 */
fun LongRange.countL(): Long =
    // Adding 1 to range because we're counting # of elements, not distance.
    (last + 1) - first

fun LongRange.combine(other: LongRange): LongRange =
    LongRange(
        start = min(first, other.first),
        endInclusive = max(last, other.last)
    )

/**
 * Returns true if any part of this range overlaps other.
 */
fun LongRange.doesOverlap(other: LongRange): Boolean =
    contains(other.first) || contains(other.last) ||
            other.contains(first) || other.contains(last)

private tailrec fun combinePartialOverlaps(ranges: Set<LongRange>, newRange: LongRange): Set<LongRange> {
    val overlappingRange = ranges.find { range -> range != newRange && range.doesOverlap(newRange) }
    if (overlappingRange == null) return ranges

    val updatedRange = overlappingRange.combine(newRange)
    // Using sets with plus and minus to force using LongRanges instead of Iterable<Long>
    val updatedRanges = ranges.minus(setOf(overlappingRange)).plus(setOf(updatedRange))
    return combinePartialOverlaps(updatedRanges, updatedRange)
}

fun Set<LongRange>.addRangeAndCombine(newRange: LongRange): Set<LongRange> =
    when {
        // newRange is a subset of an existing range
        this.any { range -> range.contains(newRange.first) && range.contains(newRange.last) } ->
            this
        // newRange overlaps with an existing range
        this.any { range -> range.doesOverlap(newRange) } ->
            combinePartialOverlaps(this, newRange)
        // newRange is not represented at all
        else ->
            this.plus(setOf(newRange))
    }
