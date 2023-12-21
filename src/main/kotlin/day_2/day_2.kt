package day_2

import Day

class Day2 : Day() {

    private data class Game(val r: Int, val g: Int, val b: Int)

    private fun parseGames() = input
        .map {
            it.substringAfter(": ").split("; ").map {
                it.split(", ").map {
                    val color = it.substringAfter(" ")
                    val count = it.substringBefore(" ").toInt()
                    color to count
                }.let {
                    Game(
                        r = it.firstOrNull { it.first == "red" }?.second ?: 0,
                        g = it.firstOrNull { it.first == "green" }?.second ?: 0,
                        b = it.firstOrNull { it.first == "blue" }?.second ?: 0
                    )
                }
            }
        }

    override fun partA() = parseGames()
        .mapIndexed { index, s -> (index + 1) * if (s.all { it.r <= 12 && it.g <= 13 && it.b <= 14 }) 1 else 0 }
        .sum()
        .toString()

    override fun partB(): String = parseGames()
        .sumOf { it.maxOf { it.r } * it.maxOf { it.g } * it.maxOf { it.b } }
        .toString()
}