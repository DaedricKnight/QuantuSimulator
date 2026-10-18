package com.daedricknight.artem.quantumengine.ventiles.gate

import java.util.Collections

abstract class ThreeQubitGate : QGate {
    override var mainIndex = 0
    var secondIndex = 0
        private set
    var thirdIndex = 0
        private set

    constructor(first: Int, second: Int, third: Int) {
        mainIndex = first
        secondIndex = second
        thirdIndex = third
    }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        if (cnt !in 1..2) {
            System.err.println("Can't set qubit $cnt as additional on a three qubit gate")
        }
        if (cnt == 1) secondIndex = idx
        if (cnt == 2) thirdIndex = idx
    }

    override val affectedIndexes: List<Int> = listOf(mainIndex, secondIndex, thirdIndex)

    override val highestAffectedIndex: Int
        get() = Collections.max(affectedIndexes)

    override val name: String
        get() = this.javaClass.name

    override val caption: String
        get() = name

    override val group: String
        get() = "ThreeQubit"

    override fun toString(): String {
        return "Gate acting on qubits $mainIndex, $secondIndex and $thirdIndex and caption $caption"
    }
}