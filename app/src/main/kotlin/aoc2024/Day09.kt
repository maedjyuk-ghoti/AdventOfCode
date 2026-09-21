package ghoti.maedjyuk.app.aoc2024

import kotlin.text.getOrElse
import kotlin.text.windowed

class Day09(
    input: String,
) {
    val disk: String = input

    private fun expand(
        marker: Long?,
        size: Int,
    ): List<Long?> = List(size) { marker }

    private fun String.expand(): List<Long?> =
        windowed(size = 2, step = 2, partialWindows = true)
            .flatMapIndexed { index, string ->
                val fileSize = string[0].digitToInt()
                val file = expand(index.toLong(), fileSize)

                val freeSpace = string.getOrElse(1) { '0' }.digitToInt()
                val free = expand(null, freeSpace)

                file + free
            }

    private fun List<Long?>.compressPart1(): List<Long?> {
        var start = 0
        var end = lastIndex
        val mutableLayout = toMutableList()

        while (start != end) {
            if (mutableLayout[start] != null) {
                start += 1
            } else if (mutableLayout[end] == null) {
                end -= 1
            } else if (mutableLayout[start] == mutableLayout[end]) {
                break
            } else {
                mutableLayout[start] = mutableLayout[end]
                mutableLayout[end] = null
            }
        }
        return mutableLayout.toList()
    }

    private fun List<Long?>.checksum(): Long =
        mapIndexed { index, i -> index * (i ?: 0) }
            .sum()

    fun solvePart1(): Long =
        disk
            .expand()
            .compressPart1()
            .checksum()

    private fun String.expand2(): List<Pair<Int?, Long>> =
        windowed(size = 2, step = 2, partialWindows = true)
            .flatMapIndexed { index, string ->
                val fileSize = string[0].digitToInt().toLong()
                val file = index to fileSize

                val freeSpace = string.getOrElse(1) { '0' }.digitToInt().toLong()
                val free = null to freeSpace

                listOf(file, free)
            }

    private fun List<Pair<Int?, Long>>.compressPart2(): List<Pair<Int?, Long>> {
        var end = lastIndex
        val mutableLayout = toMutableList()
        val moved = mutableSetOf<Int>()

        while (end != 0) {
            if (mutableLayout[end].first == null) {
                end -= 1
            } else if (moved.contains(end)) {
                end -= 1
            } else {
                var start = 0
                while (start < end) {
                    if (mutableLayout[start].first != null) {
                        start += 1
                    } else if (mutableLayout[start].second < mutableLayout[end].second) {
                        start += 1
                    } else {
                        break
                    }
                }
                if (start == end) {
                    end -= 1
                    continue
                } else {
                    val movingFile = mutableLayout[end]
                    val freeSpace = mutableLayout[start]
                    val remainingFreeSpace = freeSpace.second - movingFile.second
                    // update remaining free space
                    mutableLayout[start] = freeSpace.first to remainingFreeSpace
                    // free old file space
                    mutableLayout[end] = freeSpace.first to movingFile.second
                    // take from free space
                    mutableLayout.add(start, movingFile)
                    // mark file as moved
                    moved.add(movingFile.first!!)
                    // don't move end, it has already moved because we shifted the underlying list
                }
            }
        }
        return mutableLayout.toList().filter { (_, size) -> size != 0L }
    }

    private fun List<Pair<Int?, Long>>.expand(): List<Long?> = flatMap { (marker, size) -> List(size.toInt()) { marker?.toLong() } }

    fun solvePart2(): Long =
        disk
            .expand2()
            .compressPart2()
            .also { println(it) }
            .expand()
            .checksum()
}
