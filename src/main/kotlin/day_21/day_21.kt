package day_21

import Day

class Day21: Day() {

    private fun step(map: Array<Array<Char>>) : Array<Array<Char>>{
        val newMap = Array(map.size) { Array(map[0].size) { '.' } }
        val newLocations = mutableListOf<Pair<Int,Int>>()
        for (y in 0 until map.size) {
            for (x in 0 until map[0].size) {
                val c = map[y][x]
                when(c) {
                    '.' -> {}
                    '#' -> { newMap[y][x] = '#'}
                    'O' -> { newLocations += y to x - 1; newLocations += y to x + 1; newLocations += y - 1 to x; newLocations += y + 1 to x }
                }
            }
        }
        newLocations.filter {
            it.first in 0 until map.size
                    && it.second in 0 until map[0].size
                    && map[it.first][it.second] != '#' }
            .forEach {
                newMap[it.first][it.second] = 'O'
            }
        return newMap
    }

    override fun partA(): String {
        var mutableMap = input.map { it.toCharArray().toTypedArray() }.toTypedArray()
        val startX = input.maxOf { it.indexOf('S') }
        val startY = input.indexOfFirst { it.contains('S') }
        mutableMap[startY][startX] = 'O'
        for (i in 0 until 64) {
            mutableMap = step(mutableMap)
        }
        return mutableMap.sumOf { it.count { it == 'O' } }.toString()
    }

    // Optimizations possible
    // Some parts are hard matching puzzle input
    // notice how wide are diagonals + central lines (vertical + horizontal) empty
    // allowing to match just
    override fun partB() = part2()
}