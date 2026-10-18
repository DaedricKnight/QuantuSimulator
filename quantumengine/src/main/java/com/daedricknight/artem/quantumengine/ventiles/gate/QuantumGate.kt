package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Single-qubit quantum gate represented by a 2x2 unitary matrix:
 * [ [m00, m01],
 *   [m10, m11] ]
 */
data class QuantumGate(
    val name: String,
    val m00: Complex, val m01: Complex,
    val m10: Complex, val m11: Complex
) {
    /**
     * Converts to 2x2 matrix representation using [Complex].
     */
    fun toMatrix(): Array<Array<Complex>> = arrayOf(
        arrayOf(Complex(m00.real, m00.imag), Complex(m01.real, m01.imag)),
        arrayOf(Complex(m10.real, m10.imag), Complex(m11.real, m11.imag))
    )

    val matrix: Array<Array<Complex>>
        get() = toMatrix()

    companion object {
        private val invSqrt2 = 1.0 / sqrt(2.0)

        /** Identity gate (I): leaves state unchanged */
        val I = QuantumGate(
            name = "I",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex.ONE
        )

        /** Pauli X gate (NOT): flips |0> <-> |1> */
        val X = QuantumGate(
            name = "X",
            m00 = Complex.ZERO, m01 = Complex.ONE,
            m10 = Complex.ONE,  m11 = Complex.ZERO
        )

        /** Pauli Y gate: rotation around Y-axis by pi */
        val Y = QuantumGate(
            name = "Y",
            m00 = Complex.ZERO,       m01 = Complex(0.0, -1.0),
            m10 = Complex(0.0, 1.0),   m11 = Complex.ZERO
        )

        /** Pauli Z gate: phase flip |1> -> -|1> */
        val Z = QuantumGate(
            name = "Z",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex(-1.0, 0.0)
        )

        /** Hadamard gate (H): transforms basis states into superposition */
        val H = QuantumGate(
            name = "H",
            m00 = Complex(invSqrt2),  m01 = Complex(invSqrt2),
            m10 = Complex(invSqrt2),  m11 = Complex(-invSqrt2)
        )

        /** Phase gate S (rotation around Z-axis by pi/2) */
        val S = QuantumGate(
            name = "S",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex(0.0, 1.0)
        )

        /** T gate (rotation around Z-axis by pi/4) */
        val T = QuantumGate(
            name = "T",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex(cos(Math.PI / 4), sin(Math.PI / 4))
        )

        /** Measurement projection gate (represented by 2x2 identity matrix) */
        val Measurement = QuantumGate(
            name = "M",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex.ONE
        )

        /**
         * Parametric Phase Shift gate R_phi(theta):
         * |0> -> |0>
         * |1> -> e^(i * theta) * |1>
         */
        fun phaseShift(theta: Double) = QuantumGate(
            name = "Phase($theta)",
            m00 = Complex.ONE,  m01 = Complex.ZERO,
            m10 = Complex.ZERO, m11 = Complex.fromPolar(1.0, theta)
        )
    }
}

/**
 * Two-qubit quantum gate represented by a 4x4 unitary matrix of [Complex].
 */
data class TwoQubitQuantumGate(
    val name: String,
    val matrix: Array<Array<Complex>>
) {
    /**
     * Converts to 4x4 matrix representation using [Complex].
     */
    fun toMatrix(): Array<Array<Complex>> = Array(matrix.size) { r ->
        Array(matrix[r].size) { c ->
            Complex(matrix[r][c].real, matrix[r][c].imag)
        }
    }

    operator fun invoke(first: Int = 0, second: Int = 1): TwoQubitGate = object : TwoQubitGate(first, second) {
        override var matrix: Array<Array<Complex>> = this@TwoQubitQuantumGate.toMatrix()
        override val caption: String = this@TwoQubitQuantumGate.name
    }

    companion object {
        /**
         * Controlled NOT (CNOT / CX): flips target qubit when control qubit is |1>.
         */
        val CNOT = TwoQubitQuantumGate(
            name = "Cnot",
            matrix = arrayOf(
                arrayOf(Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO)
            )
        )

        /**
         * Controlled-Z (CZ): applies a phase flip to |11>.
         */
        val CZ = TwoQubitQuantumGate(
            name = "Cz",
            matrix = arrayOf(
                arrayOf(Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex(-1.0, 0.0))
            )
        )

        /**
         * SWAP gate: exchanges the states of two qubits |01> <-> |10>.
         */
        val SWAP = TwoQubitQuantumGate(
            name = "Swap",
            matrix = arrayOf(
                arrayOf(Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE)
            )
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TwoQubitQuantumGate

        if (name != other.name) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }
}

/**
 * Three-qubit quantum gate represented by an 8x8 unitary matrix of [Complex].
 */
data class ThreeQubitQuantumGate(
    val name: String,
    val matrix: Array<Array<Complex>>
) {
    /**
     * Converts to 8x8 matrix representation using [Complex].
     */
    fun toMatrix(): Array<Array<Complex>> = Array(matrix.size) { r ->
        Array(matrix[r].size) { c ->
            Complex(matrix[r][c].real, matrix[r][c].imag)
        }
    }

    operator fun invoke(a: Int = 0, b: Int = 1, c: Int = 2): ThreeQubitGate = object : ThreeQubitGate(a, b, c) {
        override var matrix: Array<Array<Complex>> = this@ThreeQubitQuantumGate.toMatrix()
        override var mainIndex: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var caption: String = this@ThreeQubitQuantumGate.name
    }

    companion object {
        /**
         * Toffoli (CCNOT): flips the third qubit if and only if both control qubits are |1>.
         */
        val TOFFOLI = ThreeQubitQuantumGate(
            name = "CCnot",
            matrix = arrayOf(
                arrayOf(Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO, Complex.ZERO),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE),
                arrayOf(Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE,  Complex.ZERO)
            )
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ThreeQubitQuantumGate

        if (name != other.name) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }
}

// 1-Qubit Gate Top-Level Instances
val X = QuantumGate.X
val Y = QuantumGate.Y
val Z = QuantumGate.Z
val H = QuantumGate.H
val Hadamard = QuantumGate.H
val Identity = QuantumGate.I
val I = QuantumGate.I
val Measurement = QuantumGate.Measurement

// 2-Qubit Gate Top-Level Instances
val Cnot = TwoQubitQuantumGate.CNOT
val Cz = TwoQubitQuantumGate.CZ
val Swap = TwoQubitQuantumGate.SWAP

// 3-Qubit Gate Top-Level Instances
val Toffoli = ThreeQubitQuantumGate.TOFFOLI
