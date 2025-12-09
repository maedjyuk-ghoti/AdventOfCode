package ghoti.maedjyuk.app

object Day02 {
    private const val REGEX_STRING = "^(\\d+)\\1+$"

    private fun inputToRanges(input: String): List<LongRange> =
        input.split(',')
            .map { range ->
                range.split('-').let { (low, high) ->
                    LongRange(low.toLong(), high.toLong())
                }
            }

    private fun isIdInvalid(id: String): Boolean {
        val length = id.length

        return if (length % 2 != 0) {
            // if there are an odd number of digits then it can't be invalid
            false
        } else {
            // check for invalid id
            id.take(length / 2) == id.takeLast(length / 2)
        }
    }

    private fun getInvalidIds(range: LongRange): List<Long> =
        range.filter { id -> isIdInvalid(id.toString()) }

    fun getInvalidIdSum(input: String): Long =
        inputToRanges(input)
            .flatMap(::getInvalidIds)
            .sum()

    private fun isIdInvalid2(id: String): Boolean =
        id.matches(REGEX_STRING.toRegex())

    private fun getInvalidIds2(range: LongRange): List<Long> =
        range.filter { id -> isIdInvalid2(id.toString()) }

    fun getInvalidIdSum2(input: String): Long =
        inputToRanges(input)
            .flatMap(::getInvalidIds2)
            .sum()
}