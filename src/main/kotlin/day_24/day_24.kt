package day_24

import Day
import io.ksmt.KContext
import io.ksmt.solver.KSolverStatus
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.utils.getValue
import kotlin.time.Duration.Companion.seconds

class Day24 : Day() {

    data class Hail(val position: Vector, val speed: Vector) {
        operator fun times(iterations: Int) = Vector(
            position.x + speed.x * iterations,
            position.y + speed.y * iterations,
            position.z + speed.z * iterations
        )
    }

    data class Vector(val x: Long, val y: Long, val z: Long)
    data class VectorD(val x: Double, val y: Double, val z: Double)
    data class LineEquation(val a: Double, val b: Double) {
        fun intersects(that : LineEquation) : VectorD {
            val x = (that.b - this.b) / (this.a - that.a)
            val y = this.a * x + this.b
            return VectorD(x, y, 0.0)
        }
    }

    override fun partA(): String {
        val testAreaX = 200000000000000.0
        val testAreaY = 400000000000000.0

        val hailstones = input.map { val (pos, speed) = it.split(" @ ").map { val (x, y, z) = it.split(", ").map { it.trim().toLong() }; Vector(x, y, z) }; Hail(pos, speed) }

        val lineEquations = hailstones.map { hail ->
            val x1 = hail.position.x
            val x2 = hail.position.x + hail.speed.x
            val y1 = hail.position.y
            val y2 = hail.position.y + hail.speed.y

            if (x2 == x1) {
                // LineEquation(x1.toDouble(), 0.0)
                throw UnsupportedOperationException("straight down not supported")
            } else {
                val m = (y2 - y1).toDouble() / (x2 - x1)
                val b = y1 - m * x1
                val a = m
                LineEquation(a, b)
            }
        }

        var result = 0
        for (i in 0..<lineEquations.size - 1) {
            for (j in i + 1..<lineEquations.size) {
                val intersection = lineEquations[i].intersects(lineEquations[j])
                if (
                    intersection.x in testAreaX..testAreaY &&
                    intersection.y in testAreaX..testAreaY
                    ) {
                    // forward in time
                    val hail1 = hailstones[i]
                    val hail2 = hailstones[j]
                    if (
                        ((hail1.speed.x > 0 && intersection.x > hail1.position.x) || (hail1.speed.x < 0 && intersection.x < hail1.position.x)) &&
                        ((hail1.speed.y > 0 && intersection.y > hail1.position.y) || (hail1.speed.y < 0 && intersection.y < hail1.position.y)) &&
                        ((hail2.speed.x > 0 && intersection.x > hail2.position.x) || (hail2.speed.x < 0 && intersection.x < hail2.position.x)) &&
                        ((hail2.speed.y > 0 && intersection.y > hail2.position.y) || (hail2.speed.y < 0 && intersection.y < hail2.position.y))) {
                        result++
                    }
                }
            }
        }

        return result.toString()
    }

    override fun partB(): String {
        val hailstones = input.map { val (pos, speed) = it.split(" @ ").map { val (x, y, z) = it.split(", ").map { it.trim().toLong() }; Vector(x, y, z) }; Hail(pos, speed) }

        with(KContext()) {
            KZ3Solver(this).use { solver -> // create a Z3 SMT solver instance

                val x by intSort
                val y by intSort
                val z by intSort
                val vx by intSort
                val vy by intSort
                val vz by intSort

                var u = 1
                for ((pos, vel) in hailstones) {
                    val t1 = mkConst("t${u++}", mkIntSort())
                    solver.assert((pos.x.expr + (vel.x.expr * t1)) eq (x + (vx * t1)))
                    solver.assert((pos.y.expr + (vel.y.expr * t1)) eq (y + (vy * t1)))
                    solver.assert((pos.z.expr + (vel.z.expr * t1)) eq (z + (vz * t1)))
                }

                solver.check(1.seconds)     // WTF? single check never finds solution
                val check = solver.check()

                if (check == KSolverStatus.SAT) {
                    return solver.model().eval(x + y + z).toString()
                } else {
                    return ""
                }
            }
        }
    }
}