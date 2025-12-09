package ghoti.maedjyuk.app.utilities

fun <T> List<List<T>>.rotateCounterClockwise(filler: T): List<List<T>> {
    val maxLength = this.maxOf(List<T>::size)

    return List(maxLength) { i ->
        List(this.size) { j ->
            this.getOrElse(j) { emptyList() }
                .getOrElse(maxLength - i - 1) { filler }
        }
    }
}
