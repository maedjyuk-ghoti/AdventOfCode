package ghoti.maedjyuk.app.utilities

// Source - https://stackoverflow.com/a
// Posted by Roland
// Retrieved 2025-12-09, License - CC BY-SA 4.0
fun <T> Sequence<T>.takeWhileInclusive(predicate: (T) -> Boolean) = sequence {
    with(iterator()) {
        while (hasNext()) {
            val next = next()
            yield(next)
            if (!predicate(next)) break
        }
    }
}
