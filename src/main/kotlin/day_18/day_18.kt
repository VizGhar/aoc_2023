package day_18

import Day

class Day18 : Day() {

    private val baseSize = 1000

    private enum class Direction(val dx: Int, val dy: Int, val symbol: Char) { L(-1, 0, '<'), U(0, -1, '^'), R(1, 0, '>'), D(0, 1, 'v') }
    private data class Position(val x: Int, val y: Int)
    private data class Instruction(val direction: Direction, val amount: Int, val color: String)

    private operator fun Position.plus(direction: Direction) = Position(x + direction.dx, y + direction.dy)

    private val instructions by lazy {
        input.map { Instruction(
            Direction.valueOf(it.substringBefore(" ")),
            it.substringAfter(" ").substringBefore(" ").toInt(),
            it.substringAfter("(").substringBefore(")"))
        }
    }

    override fun partA(): String {
        val map = Array(baseSize) { Array(baseSize) { '.' } }
        var position = Position(baseSize/2, baseSize/2)
        instructions.forEach { instruction ->
            repeat(instruction.amount) {
                if (instruction.direction in listOf(Direction.U, Direction.D)) {
                    map[position.y][position.x] = instruction.direction.symbol
                }
                position += instruction.direction
                map[position.y][position.x] = instruction.direction.symbol
            }
        }

        for (y in map.indices) {
            var a = 0
            for (x in map[0].indices) {
                if (map[y][x] == '^') {
                    if (a <= 0) a = 1
                } else if (map[y][x] == 'v') {
                    a = 0
                }
                if (map[y][x] in listOf('<','>','^','v') || a == 1) {
                    map[y][x] = '#'
                }
            }
        }
        return map.sumOf { it.count { it == '#' } }.toString()
    }

    override fun partB() = part2()

}