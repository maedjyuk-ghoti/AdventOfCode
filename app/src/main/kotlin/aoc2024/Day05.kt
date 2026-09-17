package ghoti.maedjyuk.app.aoc2024

class Day05(
    input: String,
) {
    val pageOrders: Map<Int, List<Int>>
    val updates: List<List<Int>>

    init {
        val (pos, ups) = input.split(System.lineSeparator() + System.lineSeparator())

        pageOrders =
            pos
                .split(System.lineSeparator())
                .map { p ->
                    val (k, v) = p.split('|')
                    k.toInt() to v.toInt()
                }.groupBy({ it.first }, { it.second })

        updates = ups.split(System.lineSeparator()).map { u -> u.split(',').map(String::toInt) }
    }

    private fun List<Int>.isOrdered(): Boolean = isOrdered(emptyList(), this)

    private tailrec fun isOrdered(
        previousPages: List<Int>,
        nextPages: List<Int>,
    ): Boolean {
        if (nextPages.isEmpty()) return true

        val isOrdered =
            pageOrders
                .getOrDefault(nextPages.first(), emptyList())
                .none { p -> previousPages.contains(p) }

        return isOrdered &&
            isOrdered(previousPages + nextPages.first(), nextPages.drop(1))
    }

    private fun List<Int>.getMiddleElement(): Int = get(size / 2)

    private fun List<Int>.order(): List<Int> = order(emptyList(), this) ?: emptyList()

    private fun order(
        currentOrder: List<Int>,
        unordered: List<Int>,
    ): List<Int>? {
        if (!currentOrder.isOrdered()) return null
        if (unordered.isEmpty()) return currentOrder

        val nextPage = unordered.first()
        return (currentOrder.size downTo 0)
            .firstNotNullOf { i ->
                val newOrder = currentOrder.take(i) + nextPage + currentOrder.drop(i)
                order(newOrder, unordered.drop(1))
            }
    }

    fun solvePart1(): Int =
        updates
            .filter { update -> update.isOrdered() }
            .sumOf { update -> update.getMiddleElement() }

    fun solvePart2(): Int =
        updates
            .filterNot { update -> update.isOrdered() }
            .map { update -> update.order() }
            .sumOf { update -> update.getMiddleElement() }
}
