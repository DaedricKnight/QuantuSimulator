package com.daedricknight.artem.quantumengine.ventiles.gate

abstract class TwoQubitGate: QGate {
    override var mainIndex = 0
    var secondIndex = 0
        private set
    override var highestAffectedIndex = -1

    constructor(first: Int, second: Int) {
        mainIndex = first
        secondIndex = second
        highestAffectedIndex = first.coerceAtLeast(second)
    }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        secondIndex = idx
    }

    override val affectedIndexes: List<Int>
        get() = listOf(mainIndex, secondIndex)

    override val name: String
        get() = this.javaClass.name

    override val caption: String
        get() = name

    override val group: String
        get() = "TwoQubit"

    override fun toString(): String {
        return "Gate acting on qubits $mainIndex and $secondIndex and caption $caption"
    }
}