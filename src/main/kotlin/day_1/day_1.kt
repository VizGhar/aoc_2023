package day_1

import Day

class Day1 : Day() {

    override fun partA() = input.sumOf {
        it.split("").mapNotNull { it.toIntOrNull() }.let { it.first() * 10 + it.last() }
    }.toString()

    private val n = arrayOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9")

    override fun partB() = input.sumOf { line ->
        val first = n.indexOf(n.minBy { line.indexOf(it).let { if(it == -1) Int.MAX_VALUE else it } }) % 10
        val last = n.indexOf(n.maxBy { line.lastIndexOf(it).let { if(it == -1) Int.MIN_VALUE else it } }) % 10
        first * 10 + last
    }.toString()
}
