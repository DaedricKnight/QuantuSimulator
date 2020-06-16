package com.daedricknight.artem.quantumengine.ventiles

import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * Qubit class.
 */

class Qubit : IQubit {

    var theta = 0.0
    var phi = 0.0

    var mFlag = false
    var measuredValue = false

    private var probabilyty = 0.0

    fun setProbability(p: Double) { probabilyty = p }

    fun getProbability() = probabilyty

    override fun calc() = cos(theta / 2)

    override fun complexCalc() =
        Complex(cos(phi) * sin(theta / 2), sin(phi) * sin(theta / 2))

    /**
     * Performs a measurement on this qubit.
     * @return <code>0</code> or <code>1</code>
     */

    override fun measure(): Int {
        mFlag = true
        measuredValue = Math.random() < probabilyty
        return if (measuredValue) 1 else 0
    }

}