import day_1.Day1
import day_2.Day2
import day_3.Day3
import day_4.Day4
import day_5.Day5
import day_6.Day6
import day_7.Day7
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
    }
    println("All solved in $timeInMillis ms")
}

fun main() {
    Day7().solve()
}
