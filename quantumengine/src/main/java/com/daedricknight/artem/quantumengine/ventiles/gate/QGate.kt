package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex

interface QGate {
    /**
     * Return the index of the main qubit
     * @return the index of the main qubit
     */
    var mainIndex: Int

    val affectedIndexes: List<Int?>?

    /**
     * Get the highest index of the qubit that is affected by this gate
     * @return the index of the highest affected qubit
     */
    val highestAffectedIndex: Int
    val caption: String?
    val name: String?
    val group: String?
    val matrix: Array<Array<Complex>>

    fun setAdditionalQubit(idx: Int, cnt: Int)
}