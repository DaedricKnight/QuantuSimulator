package com.daedricknight.artem.quantumengine.ventiles

import com.daedricknight.artem.quantumengine.ventiles.gate.QuantumGate
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * ComplexSimple number z = real + i * imag for quantum probability amplitudes.
 */
data class Complex(val real: Double, val imag: Double = 0.0) {
    operator fun plus(other: Complex) = Complex(real + other.real, imag + other.imag)
    operator fun minus(other: Complex) = Complex(real - other.real, imag - other.imag)
    operator fun times(other: Complex) = Complex(
        real * other.real - imag * other.imag,
        real * other.imag + imag * other.real
    )
    operator fun times(scalar: Double) = Complex(real * scalar, imag * scalar)

    /** Squared magnitude of the amplitude: |z|^2 = real^2 + imag^2 */
    fun absSquared(): Double = real * real + imag * imag

    companion object {
        val ZERO = Complex(0.0, 0.0)
        val ONE = Complex(1.0, 0.0)

        /** Euler's formula: e^(i * theta) = cos(theta) + i * sin(theta) */
        fun fromPolar(radius: Double = 1.0, theta: Double) =
            Complex(radius * cos(theta), radius * sin(theta))
    }
}

/**
 * Quantum state of a qubit |psi> = alpha * |0> + beta * |1>.
 * Invariant: |alpha|^2 + |beta|^2 == 1.0
 */
class QuantumState(
    var alpha: Complex = Complex.ONE,
    var beta: Complex = Complex.ZERO
) {
    init {
        normalize()
    }

    /** Applies a 2x2 unitary gate to the state vector [alpha, beta]^T */
    fun applyGate(gate: QuantumGate): QuantumState {
        val newAlpha = (gate.m00 * alpha) + (gate.m01 * beta)
        val newBeta  = (gate.m10 * alpha) + (gate.m11 * beta)
        alpha = newAlpha
        beta = newBeta
        normalize()
        return this
    }

    /** Probability of measuring state |0>: P(0) = |alpha|^2 */
    fun probabilityZero(): Double = alpha.absSquared()

    /** Probability of measuring state |1>: P(1) = |beta|^2 */
    fun probabilityOne(): Double = beta.absSquared()

    private fun normalize() {
        val norm = sqrt(alpha.absSquared() + beta.absSquared())
        if (norm > 1e-12 && abs(norm - 1.0) > 1e-9) {
            alpha = Complex(alpha.real / norm, alpha.imag / norm)
            beta = Complex(beta.real / norm, beta.imag / norm)
        }
    }
}