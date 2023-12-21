package day_20

import Day
import java.lang.IllegalStateException

class Day20 : Day() {

    private fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    private fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }


    private enum class Pulse {
        LOW, HIGH
    }

    private data class Action(val pulse: Pulse, val source: String, val destination: String)

    private sealed class Module(
        open val name: String,
        open val destinations: List<String>
    ) {

        data class BroadcasterModule(override val name: String, override val destinations: List<String>): Module(name, destinations) {
            override fun process(source: String, pulse: Pulse): List<Action> {
                return destinations.map { Action(pulse, name, it) }
            }
        }

        data class ConjunctionModule(
            override val name: String,
            override val destinations: List<String>,
            var sources: MutableMap<String, Pulse>): Module(name, destinations) {

            override fun process(source: String, pulse: Pulse): List<Action> {
                sources[source] = pulse
                return if (sources.all { it.value == Pulse.HIGH }) destinations.map{ Action(Pulse.LOW, name, it) }
                else destinations.map{ Action(Pulse.HIGH, name, it) }
            }
        }

        data class FlipFlopModule(
            override val name: String,
            override val destinations: List<String>,
            var on: Boolean = false
        ): Module(name, destinations) {
            override fun process(source: String, pulse: Pulse): List<Action> {
                return if (pulse == Pulse.LOW) {
                    on = !on
                    destinations.map { Action(if (on) Pulse.HIGH else Pulse.LOW, name, it) }
                } else {
                    emptyList()
                }
            }
        }

        abstract fun process(source: String, pulse: Pulse): List<Action>
    }

    private val modules = input.map {
        val prefix = it.substringBefore(" ")
        val name = prefix.substring(1)
        val type = prefix[0]
        val targets = it.substringAfter(" -> ").split(", ")
        if (prefix == "broadcaster") Module.BroadcasterModule("broadcaster", targets)
        else when(type) {
            '%' -> Module.FlipFlopModule(name, targets)
            '&' -> Module.ConjunctionModule(name, targets, mutableMapOf())
            else -> throw IllegalArgumentException()
        }
    }

    init {
        // adjust conjunction modules sources list
        val conjunctionModules = modules.filterIsInstance<Module.ConjunctionModule>()
        modules.forEach { source ->
            source.destinations.forEach { sourceDestination ->
                conjunctionModules.firstOrNull { it.name == sourceDestination }?.let { it.sources[source.name] = Pulse.LOW }
            }
        }
    }

    // modules have mutable properties -> need to copy them manually
    private fun copy() = modules.map { when(it) {
        is Module.BroadcasterModule -> it.copy()
        is Module.ConjunctionModule -> it.copy()
        is Module.FlipFlopModule -> it.copy()
    } }

    private fun List<Module>.pushButton(onAction: (Action) -> Unit) {
        val broadcaster = this.first { it is Module.BroadcasterModule }
        val actions = mutableListOf<Action>()
        val firstAction = broadcaster.process("", Pulse.LOW)
        onAction(Action(Pulse.LOW, "button", broadcaster.name))
        actions.addAll(firstAction)
        while (actions.isNotEmpty()) {
            val action = actions.removeAt(0)
            onAction(action)
            val destination = firstOrNull { it.name == action.destination }
            destination?.let {
                actions.addAll(it.process(action.source, action.pulse))
            }
        }
    }

    override fun partA(): String {
        val actualConfiguration = copy()
        var lows = 0
        var highs = 0
        for (i in 0 until 1000) {
            actualConfiguration.pushButton {
                if (it.pulse == Pulse.LOW) lows++ else highs++
            }
        }
        return (highs * lows).toString()
    }

    override fun partB() :String {
        val actualConfiguration = copy()

        val parentNode = actualConfiguration.first { it.destinations.contains("rx") }
        val grandParentNodes = actualConfiguration.filter { it.destinations.contains(parentNode.name) }.toMutableList()
        if (grandParentNodes.any { it.destinations.size > 1 }) throw IllegalStateException()

        // in my case all grandparent nodes are &nodes (there are 4 of them)
        // all grandparent nodes must receive at least one LOW pulse = emit HIGH pulse = parent node emits LOW pulse

        val configuration = copy()
        val resolutions = Array(grandParentNodes.size) { 0L }
        var presses = 0L
        while (resolutions.any { it == 0L }) {
            presses++
            configuration.pushButton { action ->
                val matching = grandParentNodes.filter { grandparent -> action.destination == grandparent.name && action.pulse == Pulse.LOW }
                for (g in matching) {
                    resolutions[grandParentNodes.size - 1] = presses
                    grandParentNodes.remove(g)
                }
            }
        }
        return findLCMOfListOfNumbers(resolutions.toList()).toString()
    }
}