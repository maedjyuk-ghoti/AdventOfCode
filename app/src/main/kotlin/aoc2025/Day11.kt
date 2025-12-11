package ghoti.maedjyuk.app.aoc2025

object Day11 {
    private fun parseInput(input: String): Map<String, List<String>> =
        input.lines()
            .associate { line ->
                val (node, adjacentNodesStr) = line.split(':')
                val adjacentNodes = adjacentNodesStr.trim().split(' ')
                node to adjacentNodes
            }

    private fun Map<String, List<String>>.getPathsFrom(
        start: String,
        end: String,
        visited: List<String>
    ): List<List<String>> {
        // Check for cycles
        if (visited.contains(start)) return emptyList()

        // Check for end
        val visitedUpdated = visited.plus(start)
        if (start == end) return listOf(visitedUpdated)

        // Go next
        return this.getOrElse(start, ::emptyList)
            .flatMap { adjacentNode -> getPathsFrom(adjacentNode, end, visitedUpdated) }
            .filter(List<*>::isNotEmpty)
    }

    fun getNumberOfPathsOut(input: String): Int =
        parseInput(input)
            .getPathsFrom("you", "out", emptyList())
            .size

    private fun Map<String, List<String>>.getNumberOfPaths(
        start: String,
        end: String,
        memo: MutableMap<String, Long>
    ): Long {
        if (start == end) return 1
        if (memo.containsKey(start)) return memo.getValue(start)

        return this.getOrElse(start, ::emptyList)
            .sumOf { adjacentNode -> getNumberOfPaths(adjacentNode, end, memo) }
            .also { memo[start] = it }
    }

    fun getNumberOfPathsOut2(input: String): Long =
        parseInput(input)
            .let { adjacencyList ->
                adjacencyList.getNumberOfPaths("svr", "fft", mutableMapOf()) *
                        adjacencyList.getNumberOfPaths("fft", "dac", mutableMapOf()) *
                        adjacencyList.getNumberOfPaths("dac", "out", mutableMapOf())
            }
}
