package day_16

import Day
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class Day16 : Day() {

    private enum class Orientation(
        val dx: Int,
        val dy: Int,
        val hitVert: () -> List<Orientation>, // |
        val hitHor: () -> List<Orientation>,  // -
        val hitA: () -> List<Orientation>,    // \
        val hitB: () -> List<Orientation>     // /
    ) {
        L(-1, 0, { listOf(T, B) }, { listOf(L) }, { listOf(T) }, { listOf(B) }),
        T(0, -1, { listOf(T) }, { listOf(L, R) }, { listOf(L) }, { listOf(R) }),
        R(1, 0, { listOf(T, B) }, { listOf(R) }, { listOf(B) }, { listOf(T) }),
        B(0, 1, { listOf(B) }, { listOf(L, R) }, { listOf(R) }, { listOf(L) });

        fun hit(char: Char) =
            when (char) {
                '|' -> hitVert()
                '-' -> hitHor()
                '\\' -> hitA()
                '/' -> hitB()
                else -> listOf(this)
            }
    }

    private data class Beam(val position: Position, val orientation: Orientation)
    private data class Position(val x: Int, val y: Int)

    private fun beam(initial: Beam): Int {
        val beams = mutableListOf(initial)
        val visited = mutableListOf<Beam>()

        while (beams.isNotEmpty()) {
            val actualBeam = beams.removeAt(0)
            val direction = actualBeam.orientation
            val (actualX, actualY) = actualBeam.position

            // out of bounds
            if (actualX !in input.indices) { continue }
            if (actualY !in 0 until input[0].length) { continue }

            // already visited
            if (actualBeam in visited) { continue }

            visited += actualBeam

            beams.addAll(direction.hit(input[actualY][actualX]).map {
                Beam(Position(actualX + it.dx, actualY + it.dy), it)
            })
        }

        return visited.distinctBy { it.position }.count()
    }

    override fun partA(): String {
        return beam(Beam(Position(0, 0), Orientation.R)).toString()
    }

    override fun partB(): String {
        val job = Job()

        val scope = object : CoroutineScope {
            override val coroutineContext: CoroutineContext
                get() = Dispatchers.Default + job
        }

        val process =
            ((input.indices).map { y -> { beam(Beam(Position(0, y), Orientation.R)) } } +
            (0..<input[0].length).map { x -> { beam(Beam(Position(x, 0), Orientation.B)) } } +
            (input.indices).map { y -> { beam(Beam(Position(input[0].length - 1, y), Orientation.L)) } } +
            (0..<input[0].length).map { x -> { beam(Beam(Position(x, input.size - 1), Orientation.T)) } }).toMutableList()

        var result = 0

        runBlocking {
            (0..<Runtime.getRuntime().availableProcessors()).map {
                scope.async {
                    while (process.isNotEmpty()) {
                        process.removeAt(0)().let { if (it > result) result = it }
                    }
                }
            }.awaitAll()
        }

        return result.toString()
    }

}