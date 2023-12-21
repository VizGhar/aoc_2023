package day_14

import Day

class Day14 : Day() {

    private data class Input(val rocks: List<Position>, val balls: List<Position>, val width: Int, val height: Int)

    private data class Position(val x: Int, val y: Int)

    private enum class Direction(val delta: Position, val sort: (Position) -> Int) {
        L(Position(-1, 0), { it.x }), T(Position(0, -1), { it.y }),
        R(Position(1, 0), { -it.x }), B(Position(0, 1), { -it.y })
    }

    private operator fun Position.plus(d: Direction) = Position(this.x + d.delta.x, this.y + d.delta.y)

    private fun parseInput() : Input {
        val map = input
        val rocks = mutableListOf<Position>()
        val balls = mutableListOf<Position>()

        for (y in map.indices) {
            for (x in 0 until map[0].length) {
                if (map[y][x] == '#') rocks += Position(x,y)
                if (map[y][x] == 'O') balls += Position(x,y)
            }
        }
        return Input(rocks, balls, map[0].length, map.size)
    }

    private fun move(
        width: Int,
        height: Int,
        rocks: List<Position>,
        balls: List<Position>,
        direction: Direction) : List<Position> {

        val sortedB = balls.sortedBy { direction.sort(it) }.toMutableList()
        val result = mutableListOf<Position>()

        while(sortedB.isNotEmpty()) {
            val ball = sortedB.removeAt(0)
            val target = ball + direction
            if (sortedB.any { target == it }) break
            if (rocks.any { target == it }) break
            if (target.y !in 0 until height) break
            if (target.x !in 0 until width) break
            result += target
        }

        return result
    }

    override fun partA(): String {
        val inp = parseInput()
        val result = move(inp.width, inp.height, inp.rocks, inp.balls, Direction.T)
        return result.sumOf { inp.height - it.y }.toString()
    }

    private fun spinCycle(input: Input) : List<Position> {
        var result = input.balls
        for (direction in listOf(Direction.T, Direction.L, Direction.B, Direction.R)) {
            result = move(input.width, input.height, input.rocks, result, direction)
        }
        return result
    }

    override fun partB(): String {
        val inp = parseInput()

        val memo = mutableListOf<List<Position>>()

        var ballList = inp.balls

        var i = 0
        while (i < 1000000000) {
            if (memo.contains(ballList)) {
                val memoPos = memo.indexOf(ballList)
                val cycleSize = i - memoPos
                var c = 1000000000
                while (c > memoPos + cycleSize) {
                    c -= cycleSize
                }
                ballList = memo[c]
                break
            }
            memo += ballList
            ballList = spinCycle(inp.copy(balls = ballList))
            i++
        }
        return ballList.sumOf { inp.height - it.y }.toString()
    }
}