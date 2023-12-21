package day_11

import Day
import kotlin.math.absoluteValue

class Day11 : Day() {

    private data class Position(val x: Int, val y: Int)

    private fun Position.manhattan(that: Position) = (this.x - that.x).absoluteValue + (this.y - that.y).absoluteValue

    private fun solve(expandTimes: Int = 1): String {
        val freeRows = input.mapIndexedNotNull { index, s -> index.takeIf { s.all { it == '.' } } }
        val freeColumns = (0 until input[0].length).filter { x -> input.map { it[x] }.all { it == '.' }}

        val galaxies = mutableListOf<Position>()

        for (y in input.indices) {
            for (x in 0 until input[0].length) {
                if (input[y][x] == '#') galaxies += Position(x, y)
            }
        }

        val movedGalaxies = galaxies.map { galaxy ->
            val rowsAbove = freeRows.count { it < galaxy.y }
            val columnsBefore = freeColumns.count { it < galaxy.x }
            Position(galaxy.x + columnsBefore * expandTimes, galaxy.y + rowsAbove * expandTimes)
        }

        val result = movedGalaxies.dropLast(1).mapIndexed { index, galaxySource -> movedGalaxies.drop(index + 1).map { galaxyTarget -> galaxySource.manhattan(galaxyTarget).toLong() } }

        return result.sumOf { it.sum() }.toString()
    }

    override fun partA() = solve(1)

    override fun partB() = solve(999_999)
}