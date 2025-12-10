package ghoti.maedjyuk.app.utilities

import kotlin.math.absoluteValue

data class Line(
    val point1: Point2D,
    val point2: Point2D
) {
    fun getPoints(): Set<Point2D> {
        val step = when {
            point1.x > point2.x -> Point2D(-1, 0)
            point1.x < point2.x -> Point2D(1, 0)
            point1.y > point2.y -> Point2D(0, -1)
            point1.y < point2.y -> Point2D(0, 1)
            else -> Point2D(0, 0)
        }

        val distanceInclusive = maxOf(
            (point1.x - point2.x).absoluteValue,
            (point1.y - point2.y).absoluteValue
        ) + 1

        return generateSequence(point1) { previousPoint -> previousPoint + step }
            .take(distanceInclusive)
            .toSet()
    }
}

/**
 * Order matters!! It has to do with which line is projected onto the other.
 * If this is a subset of other, they don't intersect
 * If other is a subset of this, they do intersect
 */
fun Line.intersects(other: Line): Boolean {
    val intersection = this.getPoints().intersect(other.getPoints())
    return when {
        // if there are no common points, then they don't intersect
        intersection.isEmpty() -> false
        // if this is contained in other, this lies on top of other
        intersection.size == this.getPoints().size -> false
        else -> true
    }
}

fun Line.crosses(other: Line): Boolean {
    val intersection = this.getPoints().intersect(other.getPoints())
    return when {
        // if there are no common points, then they don't intersect
        intersection.isEmpty() -> false
        // if this is contained in other, this lies on top of other
        intersection.size > 1 -> false
        else -> {
            val intersect = intersection.first()
            !(this.point1 == intersect ||
                    this.point2 == intersect ||
                    other.point1 == intersect ||
                    other.point2 == intersect)
        }
    }
}

fun Line.contains(point: Point2D): Boolean =
    getPoints().contains(point)