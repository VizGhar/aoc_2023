package day_4

import Day
import kotlin.math.pow

class Day4 : Day() {

    fun winnings() = input.map { line ->
        val winning = line.substringAfter(": ").substringBefore(" | ").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val actual = line.substringAfter(" | ").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        actual.count { it in winning }
    }

    override fun partA() = winnings().sumOf { count -> 2.0.pow(count - 1).toInt() }.toString()

    override fun partB() : String {
        val cardCount = Array(input.size) { 1 }
        winnings().forEachIndexed { i, count ->
            (1..count).forEach { j -> cardCount[i + j]+=cardCount[i] }
        }
        return cardCount.sum().toString()
    }

}