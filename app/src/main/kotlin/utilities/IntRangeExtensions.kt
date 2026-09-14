package ghoti.maedjyuk.app.utilities

import kotlin.math.max
import kotlin.math.min

fun IntRange.size(): Int = last - first + 1

fun IntRange.overlaps(other: IntRange): Boolean = max(first, other.first) <= min(last, other.last)
