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
    }
    println("All solved in $timeInMillis ms")
}

fun main() {
}
