package day_21

import kotlin.math.absoluteValue

private val Day21.mapSize     get() = input.size
private val Day21.mapHalfSize get() = mapSize / 2

private data class Position(val x: Int, val y: Int) {
    val isPositive = (x + y) % 2 == 1
}

private fun Position.dist(x: Int, y: Int) = (this.x - x).absoluteValue + (this.y - y).absoluteValue

private enum class SearchType(val filter: (size: Int, position: Position) -> Boolean) {
    FULL_POSITIVE(filter = { _, pos -> pos.isPositive }),
    FULL_NEGATIVE(filter = { _, pos -> !pos.isPositive }),

    LEFT(filter = { size, pos -> pos.dist(size - 1, size / 2) <= size && pos.isPositive }),
    TOP(filter = { size, pos -> pos.dist(size / 2, size - 1) <= size && pos.isPositive }),
    RIGHT(filter = { size, pos -> pos.dist(0, size / 2) <= size && pos.isPositive  }),
    BOTTOM(filter = { size, pos -> pos.dist(size / 2, 0) <= size && pos.isPositive  }),

    SMALL_TOP_LEFT(filter = { size, pos -> pos.dist(size - 1, size - 1) <= size / 2 && !pos.isPositive }),
    SMALL_TOP_RIGHT(filter = { size, pos -> pos.dist(0, size - 1) <= size / 2 && !pos.isPositive }),
    SMALL_BOTTOM_LEFT(filter = { size, pos -> pos.dist(size - 1, 0) <= size / 2 && !pos.isPositive }),
    SMALL_BOTTOM_RIGHT(filter = { size, pos -> pos.dist(0, 0) <= size / 2 && !pos.isPositive }),

    BIG_TOP_LEFT(filter = { size, pos -> pos.dist(size - 1, size - 1) <= size * 3 / 2 && pos.isPositive }),
    BIG_TOP_RIGHT(filter = { size, pos -> pos.dist(0, size - 1) <= size * 3 / 2 && pos.isPositive }),
    BIG_BOTTOM_LEFT(filter = { size, pos -> pos.dist(size - 1, 0) <= size * 3 / 2 && pos.isPositive }),
    BIG_BOTTOM_RIGHT(filter = { size, pos -> pos.dist(0, 0) <= size * 3 / 2 && pos.isPositive }),
}

private fun Day21.allPositions() : List<Position> {
    val positions = mutableListOf<Position>()
    val unresolvedPositions = mutableListOf(Position(0, 0))
    while (unresolvedPositions.isNotEmpty()) {
        val position = unresolvedPositions.removeAt(0)
        if (position in positions) continue
        if (position.x !in 0 until mapSize) continue
        if (position.y !in 0 until mapSize) continue
        if (input[position.y][position.x] == '#') continue
        positions += position
        unresolvedPositions += position.copy(x = position.x + 1)
        unresolvedPositions += position.copy(x = position.x - 1)
        unresolvedPositions += position.copy(y = position.y - 1)
        unresolvedPositions += position.copy(y = position.y + 1)
    }
    return positions
}

private fun Day21.counter(allPositions: List<Position>, type: SearchType) =
    allPositions.filter { type.filter(mapSize, it) }.size

fun Day21.part2() : String {

    // asserts
    if (input.size % 2 == 0) throw UnsupportedOperationException("Assuming map of odd length")
    if (input.size != input[0].length) throw UnsupportedOperationException("Assuming square map")
    if (input[input.size/2][input.size/2] != 'S') throw UnsupportedOperationException("Assuming S in middle")
    if (input[input.size/2].contains('#')) throw UnsupportedOperationException("Assuming DOTS only on horizontal line from S")
    if (input.map { it[input.size/2] }.contains('#')) throw UnsupportedOperationException("Assuming DOTS only on vertical line from S")

    val totalIterations = 26501365

    val allPositions = allPositions()

    val fullPlus = counter(allPositions, SearchType.FULL_POSITIVE)
    val fullMinus = counter(allPositions, SearchType.FULL_NEGATIVE)

    // after 131 (mapSize) moves you have planted seed exactly to center of neighbor map
    val sideLength       = ((totalIterations - mapHalfSize) / mapSize).toLong()    // 202300 - count of maps towards each dimension ... you end up at the far-edge of 202300th map
    val solvedSideLength = sideLength - 1   // this many maps are fully solved to the left of starting map (and to right/top/bottom)
    val negative = (solvedSideLength + 1) * (solvedSideLength + 1) // these many maps are fully filled with O in place of S at the end
    val positive = solvedSideLength * solvedSideLength             // these many maps are fully filled with . in place of S at the end

    val fullPositiveCount = positive * fullPlus
    val fullNegativeCount = negative * fullMinus
    val fullCount = fullPositiveCount + fullNegativeCount

    // farthest cells - 1 * size iterations
    val leftMap = counter(allPositions, SearchType.LEFT)
    val topMap = counter(allPositions, SearchType.TOP)
    val rightMap = counter(allPositions, SearchType.RIGHT)
    val bottomMap = counter(allPositions, SearchType.BOTTOM)
    val farCount = leftMap + topMap + rightMap + bottomMap

    // border cells - 1/2 * size iterations
    val topLeftSmall = counter(allPositions, SearchType.SMALL_TOP_LEFT)
    val topRightSmall = counter(allPositions, SearchType.SMALL_TOP_RIGHT)
    val bottomRightSmall = counter(allPositions, SearchType.SMALL_BOTTOM_RIGHT)
    val bottomLeftSmall = counter(allPositions, SearchType.SMALL_BOTTOM_LEFT)
    val smallCount = (topLeftSmall + topRightSmall + bottomRightSmall + bottomLeftSmall) * sideLength

    // border cells - 3/2 * size iterations
    val topLeftBig = counter(allPositions, SearchType.BIG_TOP_LEFT)
    val topRightBig = counter(allPositions, SearchType.BIG_TOP_RIGHT)
    val bottomRightBig = counter(allPositions, SearchType.BIG_BOTTOM_RIGHT)
    val bottomLeftBig = counter(allPositions, SearchType.BIG_BOTTOM_LEFT)
    val bigCount = (topLeftBig + topRightBig + bottomRightBig + bottomLeftBig) * (sideLength - 1)

    return (fullCount + farCount + smallCount + bigCount).toString()
}