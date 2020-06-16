package com.daedricknight.artem.quantumengine.ventiles.gate

import java.util.*

/**
 *
 * This class describe a Gate that operates on three qubits. In a single
 * `Step`, there should not be two Gates that act on the same qubit.
 *
 */
abstract class ThreeQubitGate : Gate {
    var mainQubitThreeQubitGate = 0
        get() = field
    var secondQubit = 0
        private set
    var thirdQubit = 0
        private set

    constructor() {}
    constructor(first: Int, second: Int, third: Int) {
        mainQubitThreeQubitGate = first
        secondQubit = second
        thirdQubit = third
    }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        if (cnt < 1 || cnt > 2) {
            System.err.println("Can't set qubit $cnt as additional on a three qubit gate")
        }
        if (cnt == 1) secondQubit = idx
        if (cnt == 2) thirdQubit = idx
    }

    override val affectedQubitIndexes: List<Int>
        get() = Arrays.asList(mainQubitThreeQubitGate, secondQubit, thirdQubit)

    override val highestAffectedQubitIndex: Int
        get() = Collections.max(affectedQubitIndexes)

    override val name: String
        get() = this.javaClass.name

    override var caption = name

    override var group = "ThreeQubit"

    override fun toString(): String {
        return "Gate acting on qubits " + mainQubitThreeQubitGate + ", " + secondQubit + " and " + thirdQubit + " and caption " + caption
    }
}