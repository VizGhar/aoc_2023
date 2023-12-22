package day_22

import Day

class Day22: Day() {

    private data class Position(
        val x: Int, val y: Int, val z: Int
    )

    private data class Brick(
        val name: String,
        val p1: Position,
        val p2: Position
    ) {
        fun overlaps(that: Brick) : Boolean {
            // make small cube for each x/y/z
            val thisPositions = mutableListOf<Position>()
            val thatPositions = mutableListOf<Position>()
            for (z in this.p1.z..this.p2.z) {
                for (y in this.p1.y..this.p2.y) {
                    for (x in this.p1.x..this.p2.x) {
                        thisPositions += Position(x, y, z)
                    }
                }
            }
            for (z in that.p1.z..that.p2.z) {
                for (y in that.p1.y..that.p2.y) {
                    for (x in that.p1.x..that.p2.x) {
                        thatPositions += Position(x, y, z)
                    }
                }
            }
            return thisPositions.any { it in thatPositions }
        }

        fun movedUp() =
            Brick(name = this.name,
                p1 = this.p1.copy(z = this.p1.z + 1),
                p2 = this.p2.copy(z = this.p2.z + 1)
            )

        fun movedDown() =
            Brick(name = this.name,
                p1 = this.p1.copy(z = this.p1.z - 1),
                p2 = this.p2.copy(z = this.p2.z - 1)
            )
    }

    // Parse bricks - z axis is set to lower for p1
    private fun parseBricks() =
        input.mapIndexed { index, line ->
            val p = line.split("~").map { val (x, y, z) = it.split(",").map { it.toInt() }; Position(x, y, z) }
            val min = p.minBy { it.z }
            Brick(index.toString(), min, (p - min).first())
        }

    private data class SettleMetadata(
        val result: List<Brick>,
        val movedBrickNames: List<String>
    )

    private fun List<Brick>.settled() : SettleMetadata {
        val settled = mutableListOf<Brick>()
        val movedBricks = mutableSetOf<String>()
        for (brick in sortedBy { it.p1.z }) {
            var activeConfiguration = brick
            while (activeConfiguration.p1.z != 1) {
                val requiredConfiguration = activeConfiguration.movedDown()
                if (settled.any { requiredConfiguration.overlaps(it) }) {
                    break
                } else {
                    activeConfiguration = requiredConfiguration
                    movedBricks += activeConfiguration.name
                }
            }
            settled += activeConfiguration
        }
        return SettleMetadata(settled, movedBricks.toList())
    }

    override fun partA(): String {
        val settled = parseBricks().settled().result.sortedBy { it.p1.z }

        val result = settled.count { brick ->
            val rest = settled - brick
            val movedUp = brick.movedUp()

            val aboveBricks = rest.filter { it.overlaps(movedUp) }

            aboveBricks.none { aboveBrick ->
                val aboveRest = settled - aboveBrick
                val movedDown = aboveBrick.movedDown()
                aboveRest.count { it.overlaps(movedDown) } == 1
            }
        }

        return result.toString()
    }

    override fun partB(): String {
        val settled = parseBricks().settled()
        val totalFalls = settled.result.sumOf { brick ->
            (settled.result.filter { it.name != brick.name }).settled().movedBrickNames.size
        }
        return totalFalls.toString()
    }
}