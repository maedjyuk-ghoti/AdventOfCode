package ghoti.maedjyuk.app.utilities

data class Point2D(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Point2D): Point2D =
        Point2D(
            x = x + other.x,
            y = y + other.y,
        )

    operator fun minus(other: Point2D): Point2D =
        Point2D(
            x = x - other.x,
            y = y - other.y,
        )

    companion object {
        val LEFT = Point2D(-1, 0)
        val UP = Point2D(0, -1)
        val RIGHT = Point2D(1, 0)
        val DOWN = Point2D(0, 1)
    }
}
