package day_25

import Day
import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph

// implementation("org.jgrapht:jgrapht-core:1.5.2")

class Day25 : Day() {

    override fun partA(): String {
        val graph = SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java).apply {
            input.forEach { line ->
                val from = line.substringBefore(":")
                line.substringAfter(": ").split(" ").forEach { to ->
                    addVertex(from)
                    addVertex(to)
                    addEdge(from, to)
                }
            }
        }

        val minCut = StoerWagnerMinimumCut(graph).minCut()
        return (minCut.size * (graph.vertexSet() - minCut).size).toString()
    }

    override fun partB() = ""

}