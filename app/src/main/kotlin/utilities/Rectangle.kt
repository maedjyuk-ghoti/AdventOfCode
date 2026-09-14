package ghoti.maedjyuk.app.utilities

import kotlin.math.max
import kotlin.math.min

class Rectangle(
    val x: IntRange,
    val y: IntRange,
) {
    val area = x.size().toLong() * y.size()

    fun overlaps(other: Rectangle): Boolean = x.overlaps(other.x) && y.overlaps(other.y)

    fun inner(): Rectangle =
        Rectangle(
            (x.first + 1) until x.last,
            (y.first + 1) until y.last,
        )

    companion object {
        fun of(
            a: Point2D,
            b: Point2D,
        ): Rectangle =
            Rectangle(
                min(a.x, b.x)..max(a.x, b.x),
                min(a.y, b.y)..max(a.y, b.y),
            )
    }
}
