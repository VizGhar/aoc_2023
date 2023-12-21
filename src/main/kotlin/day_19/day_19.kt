package day_18

import Day
import day_19.part2
import java.lang.IllegalStateException

class Day19 : Day() {

    private data class Item(val x: Int, val m: Int, val a: Int, val s: Int) {
        val value = x + m + a + s
    }

    private sealed interface Action {
        object Accept: Action
        object Reject: Action
        object Pass: Action
        data class MoveTo(val name: String): Action
    }
    private data class Rule(val condition: (Item) -> Action)
    private data class Workflow(val name: String, val rules: List<Rule>)

    private val workflows : List<Workflow>
    private val items     : List<Item>

    init {
        val workflowsm = mutableListOf<Workflow>()
        val itemsm     = mutableListOf<Item>()

        var parsingWorkflows = true
        input.forEach { line ->
            if (line.isEmpty()) {
                parsingWorkflows = false
            } else if(parsingWorkflows) {
                val name = line.substringBefore("{")
                val rules = line.substringAfter("{").substringBefore("}").split(",").map { s ->
                    when {
                        s == "A" -> Rule { Action.Accept }
                        s == "R" -> Rule { Action.Reject }
                        !s.contains("<") && !s.contains(">") -> Rule { Action.MoveTo(s) }
                        s.contains(">") -> Rule { item ->
                            val type = s.substringBefore('>')
                            val value = s.substringAfter('>').substringBefore(":").toInt()
                            val action = when(val target = s.substringAfter(":")) {
                                "A" -> Action.Accept
                                "R" -> Action.Reject
                                else -> Action.MoveTo(target)
                            }

                            when(type) {
                                "x" -> if (item.x > value) action else Action.Pass
                                "m" -> if (item.m > value) action else Action.Pass
                                "a" -> if (item.a > value) action else Action.Pass
                                "s" -> if (item.s > value) action else Action.Pass
                                else -> throw IllegalArgumentException()
                            }
                        }
                        else -> Rule { item ->
                            val type = s.substringBefore('<')
                            val value = s.substringAfter('<').substringBefore(":").toInt()
                            val action = when(val target = s.substringAfter(":")) {
                                "A" -> Action.Accept
                                "R" -> Action.Reject
                                else -> Action.MoveTo(target)
                            }

                            when(type) {
                                "x" -> if (item.x < value) action else Action.Pass
                                "m" -> if (item.m < value) action else Action.Pass
                                "a" -> if (item.a < value) action else Action.Pass
                                "s" -> if (item.s < value) action else Action.Pass
                                else -> throw IllegalArgumentException()
                            }
                        }
                    }
                }
                workflowsm += Workflow(
                    name = name,
                    rules = rules
                )
            } else {
                itemsm += Item(
                    x = line.substringAfter("x=").substringBefore(",").toInt(),
                    m = line.substringAfter("m=").substringBefore(",").toInt(),
                    a = line.substringAfter("a=").substringBefore(",").toInt(),
                    s = line.substringAfter("s=").substringBefore("}").toInt()
                )
            }
        }
        workflows = workflowsm
        items = itemsm
    }

    override fun partA(): String {

        val filtered = items.filter { item ->
            var workflow = workflows.first { it.name == "in" }
            while (true) {
                for (rule in workflow.rules) {
                    when (val condition = rule.condition(item)) {
                        is Action.Accept -> return@filter true
                        is Action.Reject -> return@filter false
                        is Action.MoveTo -> { workflow = workflows.first { it.name == condition.name }; break }
                        is Action.Pass -> {}
                        else -> throw IllegalStateException()
                    }
                }
            }
            @Suppress("UNREACHABLE_CODE")
            throw IllegalStateException()
        }
        return filtered.sumOf { it.value }.toString()
    }

    override fun partB() = part2()
}