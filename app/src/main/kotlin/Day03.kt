package ghoti.maedjyuk.app

import kotlin.math.pow

object Day03 {
    private fun List<Int>.findLargestValueIndex(): Int =
        this.foldIndexed(0) { index, largestValueIndex, value ->
                if (value > this[largestValueIndex]) {
                    index
                } else {
                    largestValueIndex
                }
            }

    private fun findLargestVoltage(batteries: List<Int>): Int {
        // search through batteries.length - 1 for largest number
        val firstBatteryIndex = batteries.take(batteries.size - 1)
            .findLargestValueIndex()

        // search through remainder of batteries for next largest number
        val secondBatteryIndexOffset = batteries.drop(firstBatteryIndex + 1)
            .findLargestValueIndex()

        // adjust to offset to real index in full batteries list
        val secondBatteryIndex = firstBatteryIndex + 1 + secondBatteryIndexOffset

        return (batteries[firstBatteryIndex] * 10) + batteries[secondBatteryIndex]
    }

    private fun inputToBatteryBanks(input: String): List<List<Int>> =
        input.split(System.lineSeparator())
            .map { batteryBank -> batteryBank.map(Char::digitToInt) }

    fun getTotalVoltage1(input: String): Int =
        inputToBatteryBanks(input)
            .sumOf(::findLargestVoltage)

    private fun findLargestVoltage2(batteries: List<Int>, depth: Int): Long {
        if (depth == 0) return 0

        val batteryIndex = batteries.take(batteries.size - (depth - 1))
            .findLargestValueIndex()

        return (batteries[batteryIndex] * 10.0.pow(depth - 1)).toLong() +
                findLargestVoltage2(batteries.drop(batteryIndex + 1), depth - 1)
    }

    fun getTotalVoltage2(input: String): Long =
        inputToBatteryBanks(input)
            .sumOf { findLargestVoltage2(it, 12) }
}
