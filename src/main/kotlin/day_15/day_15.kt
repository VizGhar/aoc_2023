package day_15

import Day

class Day15 : Day() {

    private val instructions by lazy {
        input[0].split(",")
    }

    private val hashes by lazy {
        instructions.map {
            var currentValue = 0
            for (c in it) {
                currentValue += c.code
                currentValue *= 17
                currentValue %= 256
            }
            currentValue
        }
    }


    private val hashes2 by lazy {
        instructions.map{ it.substringBefore("-").substringBefore("=") }.map {
            var currentValue = 0
            for (c in it) {
                currentValue += c.code
                currentValue *= 17
                currentValue %= 256
            }
            currentValue
        }
    }

    override fun partA(): String {
        return hashes.sum().toString()
    }

    override fun partB(): String {
        val boxes = List(256) { mutableListOf<Pair<String, Int>>() }
        hashes2.forEachIndexed { i, hash ->
            val instruction = instructions[i]
            if (instruction.endsWith("-")) {
                val label = instruction.substringBefore("-")
                boxes[hash].removeIf { it.first == label }
            } else {
                val label = instruction.substringBefore("=")
                val focalLength = instruction.substringAfter("=").toInt()
                val existing = boxes[hash].indexOfFirst { it.first == label }
                if (existing != -1) {
                    boxes[hash][existing] = label to focalLength
                } else {
                    boxes[hash] += label to focalLength
                }
            }
        }
        return boxes.mapIndexed { bid, box ->
            val boxId = bid + 1
            box.mapIndexed { sId, pair ->
                val slotId = sId + 1
                boxId * slotId * pair.second
            }.sum()
        }.sum().toString()
    }

}