package day_23
import Day

class Day23 : Day() {

    private data class Position(val x: Int, val y: Int)
    private data class SubRoute(val from: Position, val to: Position, val length: Int)
    private data class Route(val positions: List<Position>)

    private fun routesToClosestCrossroads(
        allCrossRoads: List<Position>,
        startPosition: Position,
        filter: (from: Position, to: Position) -> Boolean
    ): List<SubRoute> {
        val remainingCrossroads = allCrossRoads - startPosition
        val activeRoutes = mutableListOf(Route(mutableListOf(startPosition)))
        val resultRoutes = mutableListOf<SubRoute>()

        while (activeRoutes.isNotEmpty()) {
            val route = activeRoutes.removeAt(0)
            val position = route.positions.last()
            if (position.x < 0 || position.x > input[0].length - 1) continue
            if (position.y < 0 || position.y > input.size - 1) continue
            if (input[position.y][position.x] == '#') continue
            if (position in route.positions.dropLast(1)) continue
            if (position in remainingCrossroads) {
                resultRoutes += SubRoute(route.positions.first(), route.positions.last(), route.positions.size - 1)
                continue
            }

            for (newPosition in listOf(
                Position(position.x, position.y + 1),
                Position(position.x, position.y - 1),
                Position(position.x + 1, position.y),
                Position(position.x - 1, position.y)
            )) {
                if (filter(position, newPosition)) activeRoutes += Route(route.positions + newPosition)
            }
        }

        // longer first -> if there are 2 routes from-to same positions remove shorter
        // if route is cycle - skip
        return resultRoutes
            .filter { it.from != it.to }
            .sortedByDescending { it.length }
            .distinctBy { it.from to it.to }
    }

    private fun getSubRoutes(
        filter: (from: Position, to: Position) -> Boolean
    ) : Map<Position, List<SubRoute>> {
        val startPosition = Position(input.first().indexOfFirst { it == '.' }, 0)
        val endPosition = Position(input.last().indexOfFirst { it == '.' }, input.size - 1)
        val crossroads = mutableListOf(startPosition, endPosition)
        for (y in 1..<input.size) {
            for (x in 1..<input[0].length - 1) {
                if (input[y][x] == '#') continue
                val destinations = listOf(
                    input[y][x - 1] == '#',
                    input[y][x + 1] == '#',
                    input[y - 1][x] == '#',
                    runCatching { input[y + 1][x] == '#' }.getOrElse { true }
                )
                if (destinations.count { it } < 2) crossroads += Position(x, y)
            }
        }

        return crossroads.associateWith { routesToClosestCrossroads(crossroads, it, filter) }
    }

    private fun findLongest(
        from: Position,
        to: Position,
        availableSubRoutes: Map<Position, List<SubRoute>>
    ) : Int {
        if (from == to) return 0
        val routes = availableSubRoutes[from] ?: return -1000000
        val res = routes.maxOf { route ->
            route.length + findLongest(route.to, to, availableSubRoutes - from)
        }
        return res
    }

    private val startPosition = Position(input.first().indexOfFirst { it == '.' }, 0)
    private val endPosition = Position(input.last().indexOfFirst { it == '.' }, input.size - 1)

    override fun partA(): String {
        return findLongest(startPosition, endPosition, getSubRoutes { a, b ->
            when {
                input[a.y][a.x] == '>' && b.x != a.x + 1 -> false
                input[a.y][a.x] == '<' && b.x != a.x - 1 -> false
                input[a.y][a.x] == '^' && b.y != a.y - 1 -> false
                input[a.y][a.x] == 'v' && b.y != a.y + 1 -> false
                else -> true
            }
        }).toString()
    }

    override fun partB(): String {
        return findLongest(startPosition, endPosition, getSubRoutes { _, _ -> true }).toString()
    }
}
