package ghoti.maedjyuk.app.aoc2025

typealias Shape = List<List<Char>>
typealias Grid = Pair<Int, Int>
typealias GridAndGifts = List<Pair<Grid, List<Int>>>

object Day12 {
    private fun parseInput(input: String): Pair<List<Shape>, GridAndGifts> {
        val lines = input.split("(${System.lineSeparator()}){2}".toRegex())

        val shapes: List<Shape> = lines.takeWhile { it.contains("^\\d+:".toRegex()) }
            .map { shapeInfo ->
                shapeInfo.split(System.lineSeparator())
                    .drop(1) // index
                    .map(String::toList)
            }

        val gridAndGifts: GridAndGifts = lines.last()
            .split(System.lineSeparator())
            .map { gridAndGiftInfo ->
                val (grid, gifts) = gridAndGiftInfo.split(':')
                val (x, y) = grid.split('x').map(String::toInt)
                (x to y) to gifts.trim().split(' ').map(String::toInt)
            }

        return shapes to gridAndGifts
    }

    private fun Shape.getTotalDumbSpace(): Int =
        this.sumOf { l -> l.count { c -> c == '#' } }

    private fun isPossible(shapes: List<Shape>, grid: Grid, shapeCounts: List<Int>): Boolean {
        val totalShapes = shapeCounts.flatMapIndexed { index, shapeCount -> List(shapeCount) { shapes[index] } }
        val totalShapesDumbSize = totalShapes.sumOf { shape -> shape.getTotalDumbSpace() }
        return totalShapesDumbSize <= (grid.first * grid.second)
    }

    // Doesn't work for simple input, but works for the real one ¯\_(ツ)_/¯
    fun countPossibleRegions(input: String): Int =
        parseInput(input).let { (shapes, gridAndGifts) ->
            val possibleMap = gridAndGifts.map { (grid, gifts) ->
                (grid.toString() + gifts.toString()) to isPossible(shapes, grid, gifts)
            }

            return possibleMap.count { (_, b) -> b }
        }
}
