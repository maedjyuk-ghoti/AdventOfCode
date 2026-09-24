package ghoti.maedjyuk.app.utilities

fun <T> List<T>.cartesianProduct(other: List<T>): List<Pair<T, T>> =
    this.flatMap { a ->
        other.map { b ->
            a to b
        }
    }

operator fun <T> List<List<T>>.get(at: Point2D): T? = getOrNull(at.y)?.getOrNull(at.x)
