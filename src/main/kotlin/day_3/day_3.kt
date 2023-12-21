package day_3

import Day
import java.awt.Rectangle

class Day3 : Day() {

    override fun partA(): String {
        return input.mapIndexed { y, line ->
            Regex("([0-9]+)").findAll(line).sumOf { match ->
                if ((-1..1).any {
                        val subs = input[(y + it).coerceIn(0, input.size - 1)].substring(
                            maxOf(match.range.first - 1, 0),
                            minOf(match.range.last + 2, line.length - 1)
                        )
                        subs.any { it != '.' && !it.isDigit() }
                    }) match.value.toInt() else 0
            }
        }.sum().toString()
    }

    override fun partB(): String {
        val nums = input.mapIndexed { y, line -> Regex("([0-9]+)").findAll(line).toList().map { match -> Rectangle(match.range.first, y, match.range.count(), 1) to match.value.toInt() } }.flatten()
        val gears = input.mapIndexed { y, line -> Regex("(\\*)").findAll(line).toList().map { match -> Rectangle(match.range.first, y, 1, 1) } }.flatten()
        return gears.sumOf { gear ->
            val adjacent = nums.filter { num -> Rectangle(num.first.x - 1, num.first.y - 1, num.first.width + 2, 3).contains(gear) }
            if (adjacent.size == 2) (adjacent[0].second * adjacent[1].second) else 0
        }.toString()
    }
}