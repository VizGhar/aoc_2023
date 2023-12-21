package day_12

import Day

class Day12: Day() {

    private data class Input(val map: String, val combinations: List<Int>)

    private val memo = mutableMapOf<Input, Long>()

    private fun solve(input: Input): Long {
        if (memo.contains(input)) return memo[input]!!
        if (input.combinations.isEmpty()) { return if (input.map.contains('#')) 0 else 1 }

        val first = input.combinations.first()

        val firstHash = input.map.indexOf('#')
        val result = input.map.windowed(first).mapIndexedNotNull { startIndex, window ->
            if (firstHash != -1 && startIndex > firstHash) return@mapIndexedNotNull null
            val beforeIndex = startIndex - 1
            val afterIndex = startIndex + first

            // can't be beside # symbols
            if (beforeIndex >= 0 && input.map[beforeIndex] == '#') { return@mapIndexedNotNull null }
            if (afterIndex < input.map.length && input.map[afterIndex] == '#') { return@mapIndexedNotNull null }

            // can't contain .
            startIndex.takeIf { !window.contains('.') }
        }.sumOf {
            val nextStart = minOf(it + first + 1, input.map.length)
            solve(Input(input.map.substring(nextStart), input.combinations.drop(1)))
        }
        memo[input] = result
        return result
    }

    override fun partA() = input.sumOf {
        val map = it.substringBefore(" ")
        val combinations = it.substringAfter(" ").split(",").map { it.toInt() }
        solve(Input(map, combinations))
    }.toString()

    override fun partB() = input.sumOf {
        val map = (it.substringBefore(" ") + "?").repeat(5).dropLast(1)
        val combinations = it.substringAfter(" ").split(",").map { it.toInt() }
        val ctimes = (1 until 5).fold(combinations) { a, _ -> a + combinations }
        solve(Input(map, ctimes))
    }.toString()

}