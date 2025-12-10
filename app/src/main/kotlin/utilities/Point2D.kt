package ghoti.maedjyuk.app.utilities

data class Point2D(
    val x: Int,
    val y: Int
) {
    operator fun plus(other: Point2D): Point2D =
        Point2D(
            x = x + other.x,
            y = y + other.y
        )
}
