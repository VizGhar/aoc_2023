package day_17

import Day

class Day17 : Day() {

    private data class Node(
        val x       : Int,
        val y       : Int,
        var scoreA  : Int     = Int.MAX_VALUE, // score achieved when comming from Top / Bottom
        var scoreB  : Int     = Int.MAX_VALUE, // score achieved when comming from Left / Right
        var solvedA : Boolean = false,         // solved when comming from Top / Bottom
        var solvedB : Boolean = false,         // solved when comming from Left / Right
    )

    private fun toExpand(map: Array<Array<Node>>) : Node {
        var low = Int.MAX_VALUE
        var lowResult : Node? = null
        for (y in map.indices) {
            for (x in 0 until map[0].size) {
                if (!map[y][x].solvedA && map[y][x].scoreA < low) {
                    low = map[y][x].scoreA
                    lowResult = map[y][x]
                }
                if (!map[y][x].solvedB && map[y][x].scoreB < low) {
                    low = map[y][x].scoreB
                    lowResult = map[y][x]
                }
            }
        }
        return lowResult ?: throw IllegalStateException()
    }

    private fun solve(minJump: Int, maxJump: Int) : Int {
        val dijkstraMap = Array(input.size) { y -> Array(input[0].length) { x -> Node(x, y) } }

        dijkstraMap[0][0].scoreA = 0
        dijkstraMap[0][0].scoreB = 0

        while (dijkstraMap[input.size - 1][input[0].length - 1].let { !it.solvedA || !it.solvedB }) {

            val toSolve = toExpand(dijkstraMap)

            val solvingHorizontal = !toSolve.solvedA && toSolve.scoreA != Int.MAX_VALUE // comming from top/bottom
            val solvingVertical   = !toSolve.solvedB && toSolve.scoreB != Int.MAX_VALUE // comming from left/right
            val resultH = if (solvingVertical && solvingHorizontal) { toSolve.scoreA < toSolve.scoreB } else solvingHorizontal

            if (resultH) {
                // solving horizontal part
                val actualScore = toSolve.scoreA
                for (dx in minJump..maxJump) {
                    // right
                    if (toSolve.x + dx < input[0].length) {
                        val score = actualScore + (1..dx).sumOf { dxx -> input[toSolve.y][toSolve.x + dxx].digitToInt() }
                        val previousScore = dijkstraMap[toSolve.y][toSolve.x + dx].scoreB
                        dijkstraMap[toSolve.y][toSolve.x + dx].scoreB = minOf(score, previousScore)
                    }
                    // left
                    if (toSolve.x - dx >= 0) {
                        val score = actualScore + (1..dx).sumOf { dxx -> input[toSolve.y][toSolve.x - dxx].digitToInt() }
                        val previousScore = dijkstraMap[toSolve.y][toSolve.x - dx].scoreB
                        dijkstraMap[toSolve.y][toSolve.x - dx].scoreB = minOf(score, previousScore)
                    }
                }
                dijkstraMap[toSolve.y][toSolve.x].solvedA = true
            } else {

                // solving vertical part
                val actualScore = toSolve.scoreB
                for (dy in minJump..maxJump) {
                    // down
                    if (toSolve.y + dy < input.size) {
                        val score = actualScore + (1..dy).sumOf { dyy -> input[toSolve.y + dyy][toSolve.x].digitToInt() }
                        val previousScore = dijkstraMap[toSolve.y + dy][toSolve.x].scoreA
                        dijkstraMap[toSolve.y + dy][toSolve.x].scoreA = minOf(score, previousScore)
                    }
                    // up
                    if (toSolve.y - dy >= 0) {
                        val score = actualScore + (1..dy).sumOf { dyy -> input[toSolve.y - dyy][toSolve.x].digitToInt() }
                        val previousScore = dijkstraMap[toSolve.y - dy][toSolve.x].scoreA
                        dijkstraMap[toSolve.y - dy][toSolve.x].scoreA = minOf(score, previousScore)
                    }
                }
                dijkstraMap[toSolve.y][toSolve.x].solvedB = true
            }
        }

        return dijkstraMap[input.size - 1][input[0].length - 1].let { minOf(it.scoreA, it.scoreB) }
    }

    override fun partA() = solve(1, 3).toString()

    override fun partB() = solve(4, 10).toString()
}
