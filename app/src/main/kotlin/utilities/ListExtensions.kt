package ghoti.maedjyuk.app.utilities

fun <T> List<T>.cartesianProduct(other: List<T>): List<Pair<T, T>> =
    this.flatMap { a ->
        other.map { b ->
            a to b
        }
    }
