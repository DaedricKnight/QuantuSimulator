package com.daedricknight.artem.quantumengine.ventiles


/**
 * Interface for qubits calculation and measure.
 *
 */
interface IQubit {
    fun calc(): Double
    fun complexCalc(): Complex
    fun measure(): Int
}