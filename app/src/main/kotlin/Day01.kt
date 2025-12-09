package ghoti.maedjyuk.app

import kotlin.math.abs
import kotlin.math.sign

object Day01 {
    private const val STARTING_POINT: Int = 50
    private const val LOWER_BOUND: Int = 0
    private const val UPPER_BOUND: Int = 99
    private val DIAL_POSITION_COUNT = (LOWER_BOUND .. UPPER_BOUND).count()

    enum class Rotation { Left, Right }
    data class Operation(
        val rotation: Rotation,
        val distance: Int,
    ) {
        constructor(rotation: Char, distance: Int) : this(
            rotation = when (rotation.uppercase()) {
                "L" -> Rotation.Left
                else -> Rotation.Right
            },
            distance = distance
        )

        private fun getRealNumber() =
            when (rotation) {
                Rotation.Left -> distance * -1
                Rotation.Right -> distance
            }

        fun runNoAdjustment(startingPoint: Int): Int =
            startingPoint + getRealNumber()

        fun run(startingPoint: Int): Int {
            val result = runNoAdjustment(startingPoint)

            return result % DIAL_POSITION_COUNT
        }
    }

    private fun inputToOperations(input: String) : List<Operation> =
        input.split(System.lineSeparator())
            .map { operation ->
                Operation(
                    rotation = operation[0],
                    distance = operation.drop(1).toInt()
                )
            }

    fun getPassword1(input: String): Int =
        inputToOperations(input)
            .scan(STARTING_POINT) { acc, operation -> operation.run(acc) }
                .also(::println)
                .count { dialReading -> dialReading == 0 }

    private fun getNumberOfTimesDialCrossedZero(startingPoint: Int, nextLocation: Int): Int {
        // Check if we crossed zero by changing direction
        val crossedZero = if (startingPoint.sign != 0 && startingPoint.sign != nextLocation.sign) 1 else 0

        // Check how many times we crossed zero by wrapping around
        val rotations = abs(nextLocation / DIAL_POSITION_COUNT)

        return crossedZero + rotations
    }

    fun getPassword2(input: String): Int =
        inputToOperations(input)
            .scan(Pair(STARTING_POINT, 0)) { (startingPoint, _), operation ->
                val nextLocation = operation.runNoAdjustment(startingPoint)
                val numberOfTimesDialCrossedZero = getNumberOfTimesDialCrossedZero(startingPoint, nextLocation)

                Pair(nextLocation % DIAL_POSITION_COUNT, numberOfTimesDialCrossedZero)
            }
            .also(::println)
            .sumOf { (_, rotations) -> rotations }
}
