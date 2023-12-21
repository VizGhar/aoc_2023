package day_6

import Day

class Day6 : Day() {

    private fun race(availableTime: Long, distance: Long): Int {
        return (0..availableTime).count { heldFor ->
            val speed = heldFor
            val remainingTime = availableTime - heldFor
            val passedDistance = speed * remainingTime
            passedDistance > distance
        }
    }

    override fun partA() : String {
        val times     = input[0].substringAfter(":").split(" ").mapNotNull { it.trim().toLongOrNull() }
        val distances = input[1].substringAfter(":").split(" ").mapNotNull { it.trim().toLongOrNull() }
        return times.zip(distances).fold(1L) { acc, it -> acc * race(it.first, it.second) }.toString()
    }

    override fun partB() : String {
        val times     = input[0].substringAfter(":").split(" ").mapNotNull { it.trim().toIntOrNull() }.joinToString("").toLong()
        val distances = input[1].substringAfter(":").split(" ").mapNotNull { it.trim().toIntOrNull() }.joinToString("").toLong()
        return race(times, distances).toString()
    }
}
