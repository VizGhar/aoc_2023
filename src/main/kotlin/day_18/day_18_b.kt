package day_18

import java.math.BigDecimal
import kotlin.math.absoluteValue

private enum class Direction(val dx: Long, val dy: Long) { R(1, 0), D(0, 1), L(-1, 0), U(0, -1) }
private data class Position(val x: Long, val y: Long)
private data class Instruction(val direction: Direction, val amount: Long)

private fun Position.distance(that: Position) =
    if (x == that.x) (that.y - this.y).absoluteValue else (that.x - this.x).absoluteValue

fun Day18.part2(): String {
    val instructions2 = input.map {
        val hex = (it.substringAfter("#").substringBefore(")"))
        val length = hex.dropLast(1).toLong(16)
        val direction = Direction.values()[hex.last().digitToInt()]
        Instruction(direction, length)
    }

    var minus = BigDecimal.ZERO
    var position = Position(0, 0)
    val positions = instructions2.map { instruction ->
        val from = position.copy()
        val to = position.copy(
            x = from.x + instruction.direction.dx * instruction.amount,
            y = from.y + instruction.direction.dy * instruction.amount
        )
        position = to
        val dist = from.distance(to)
        minus += BigDecimal(dist)
        to
    }.toMutableList()

    var firstPart = BigDecimal.ZERO
    var secondPart = BigDecimal.ZERO

    for (i in 0 until positions.size) {
        val j = (i + 1) % positions.size
        firstPart += BigDecimal(positions[i].x * positions[j].y)
    }

    for (i in 0 until positions.size) {
        val j = (i + 1) % positions.size
        secondPart += BigDecimal(positions[i].y * positions[j].x)
    }

    val result = (firstPart - secondPart + minus) / BigDecimal(2) + BigDecimal.ONE

    return result.toString()
}