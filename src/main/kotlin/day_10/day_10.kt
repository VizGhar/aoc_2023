package day_10

import Day

class Day10 : Day() {

    private data class Position(val x: Int, val y: Int)

    private enum class Orientation(val delta: Position) {
        LEFT(Position(-1, 0)),
        TOP(Position(0, -1)),
        RIGHT(Position(1, 0)),
        BOTTOM(Position(0, 1))
    }

    private val map = mapOf(
        ('|' to Orientation.TOP) to Orientation.TOP,
        ('|' to Orientation.BOTTOM) to Orientation.BOTTOM,
        ('-' to Orientation.RIGHT) to Orientation.RIGHT,
        ('-' to Orientation.LEFT) to Orientation.LEFT,
        ('7' to Orientation.TOP) to Orientation.LEFT,
        ('7' to Orientation.RIGHT) to Orientation.BOTTOM,
        ('J' to Orientation.BOTTOM) to Orientation.LEFT,
        ('J' to Orientation.RIGHT) to Orientation.TOP,
        ('L' to Orientation.BOTTOM) to Orientation.RIGHT,
        ('L' to Orientation.LEFT) to Orientation.TOP,
        ('F' to Orientation.LEFT) to Orientation.BOTTOM,
        ('F' to Orientation.TOP) to Orientation.RIGHT,
    )

    private val zoom = mapOf(
        '|' to listOf(".|.", ".|.", ".|."),
        '-' to listOf("...", "---", "..."),
        '7' to listOf("...", "-7.", ".|."),
        'J' to listOf(".|.", "-J.", "..."),
        'L' to listOf(".|.", ".L-", "..."),
        'F' to listOf("...", ".F-", ".|."),
        '.' to listOf("...", "...", "..."),
    )

    override fun partA(): String {
        val animStartPosY = input.indexOfFirst { it.contains("S") }
        val animStartPosX = input.mapNotNull { it.indexOf("S").let { if(it == -1) null else it } }[0]
        val input = input.map { it.replace('S', 'F') }
        val startPosition = Position(animStartPosX, animStartPosY)
        var pos = startPosition
        var orientation = if (input[animStartPosY][animStartPosX] in arrayOf('L', 'F')) Orientation.RIGHT else Orientation.LEFT

        var distance = 0
        do {
            pos = Position(pos.x + orientation.delta.x, pos.y + orientation.delta.y)
            orientation = map[input[pos.y][pos.x] to orientation] ?: break
            distance++
        } while (pos != Position(animStartPosX, animStartPosY))

        return ((distance + 1) / 2).toString()
    }

    override fun partB(): String {
        val animStartPosY = input.indexOfFirst { it.contains("S") }
        val animStartPosX = input.mapNotNull { it.indexOf("S").let { if(it == -1) null else it } }[0]
        var input = input.map { it.replace('S', 'L') }
        val startPosition = Position(animStartPosX, animStartPosY)
        var pos = startPosition
        val border = mutableListOf(pos)
        var orientation = if (input[animStartPosY][animStartPosX] in arrayOf('L', 'F')) Orientation.RIGHT else Orientation.LEFT

        do {
            pos = Position(pos.x + orientation.delta.x, pos.y + orientation.delta.y)
            border += pos
            orientation = map[input[pos.y][pos.x] to orientation]!!
        } while (pos != startPosition)

        // remove unused pipes
        input = input.mapIndexed { y, s -> s.mapIndexed { x, c -> if (Position(x,y) in border) c else '.' }.joinToString("") }

        // zoom in 3x3
        val newMap = Array(input.size * 3) { y -> Array(input[0].length * 3) { x -> ' ' } }
        for (y in 0 until input.size) {
            for (x in 0 until input[0].length) {
                val replacement = zoom[input[y][x]]!!
                for (sx in 0..2) { for (sy in 0..2) { newMap[y * 3 + sy][x * 3 + sx] = replacement[sy][sx] } }
            }
        }

        // flood fill
        val toResolveStart = border.minBy { it.y * 10000 + it.x }   // always F
        val toResolve = mutableListOf(Position(toResolveStart.x * 3 + 2, toResolveStart.y * 3 + 2))
        while (toResolve.isNotEmpty()) {
            val resolving = toResolve.removeAt(0)
            if (newMap[resolving.y][resolving.x] != '.') continue
            newMap[resolving.y][resolving.x] = 'X'
            toResolve+=Position(resolving.x + 1, resolving.y)
            toResolve+=Position(resolving.x - 1, resolving.y)
            toResolve+=Position(resolving.x, resolving.y + 1)
            toResolve+=Position(resolving.x, resolving.y - 1)
        }

        val result = input.mapIndexed{ y, s -> s.mapIndexed { x, c -> input[y][x] == '.' && newMap[y*3 + 1][x*3 + 1] == 'X' }.count { it } }.sum()

        return result.toString()
    }
}