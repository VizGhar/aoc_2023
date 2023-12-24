import day_1.Day1
import day_10.Day10
import day_11.Day11
import day_12.Day12
import day_14.Day14
import day_15.Day15
import day_16.Day16
import day_17.Day17
import day_18.Day18
import day_18.Day19
import day_2.Day2
import day_20.Day20
import day_21.Day21
import day_22.Day22
import day_23.Day23
import day_24.Day24
import day_3.Day3
import day_4.Day4
import day_5.Day5
import day_6.Day6
import day_7.Day7
import day_8.Day8
import day_9.Day9
import java.io.File
import kotlin.system.measureTimeMillis

abstract class Day {
    val input by lazy { File("input/aoc_2023_${this::class.simpleName!!.takeLastWhile { it.isDigit() }}.txt").readLines() }
    abstract fun partA() : String
    abstract fun partB() : String
    fun solve() {
        val timeInMillis = measureTimeMillis {
            println(partA())
            println(partB())
        }
        println("Day ${this::class.simpleName!!.takeLastWhile { it.isDigit() }} solved in $timeInMillis ms")
    }
}

fun runAll() {
    val timeInMillis = measureTimeMillis {
        Day1().solve()
        Day2().solve()
        Day3().solve()
        Day4().solve()
        Day5().solve()
        Day6().solve()
        Day7().solve()
        Day8().solve()
        Day9().solve()
        Day10().solve()
        Day11().solve()
        Day12().solve()
        Day13().solve()
        Day14().solve()
        Day15().solve()
        Day16().solve()
        Day17().solve()
        Day18().solve()
        Day19().solve()
        Day20().solve()
        Day21().solve()
        Day22().solve()
        Day23().solve()
        Day24().solve()
    }
    println("All solved in $timeInMillis ms")
}

fun main() {
    Day24().solve()
}
