package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.*
import com.daedricknight.artem.quantumengine.ventiles.gate.Cnot
import com.daedricknight.artem.quantumengine.ventiles.gate.Cz
import com.daedricknight.artem.quantumengine.ventiles.gate.H
import com.daedricknight.artem.quantumengine.ventiles.gate.Hadamard
import com.daedricknight.artem.quantumengine.ventiles.gate.Identity
import com.daedricknight.artem.quantumengine.ventiles.gate.Measurement
import com.daedricknight.artem.quantumengine.ventiles.gate.QuantumGate
import com.daedricknight.artem.quantumengine.ventiles.gate.Swap
import com.daedricknight.artem.quantumengine.ventiles.gate.ThreeQubitQuantumGate
import com.daedricknight.artem.quantumengine.ventiles.gate.Toffoli
import com.daedricknight.artem.quantumengine.ventiles.gate.TwoQubitQuantumGate
import com.daedricknight.artem.quantumengine.ventiles.gate.X
import com.daedricknight.artem.quantumengine.ventiles.gate.Y
import com.daedricknight.artem.quantumengine.ventiles.gate.Z
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertSame
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertNotEquals
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sqrt

private val ZERO = Complex.ZERO
private val ONE = Complex.ONE
private val MINUS_ONE = Complex(-1.0, 0.0)
private val IMAG = Complex(0.0, 1.0)
private val MINUS_IMAG = Complex(0.0, -1.0)
private val INV_SQRT2 = 1.0 / sqrt(2.0)

private fun QuantumGate.m(): Matrix = toMatrix()

private fun matrixOf(vararg rows: List<Complex>): Matrix =
    Array(rows.size) { r -> rows[r].toTypedArray() }

// =====================================================================
// Single-qubit gates
// =====================================================================

class SingleQubitGateTest {

    private val allGates = listOf(
        QuantumGate.I, QuantumGate.X, QuantumGate.Y, QuantumGate.Z,
        QuantumGate.H, QuantumGate.S, QuantumGate.T, QuantumGate.Measurement,
        QuantumGate.phaseShift(0.0), QuantumGate.phaseShift(0.5), QuantumGate.phaseShift(PI)
    )

    // ---------- matrices of every gate ----------

    @Test
    fun `I matrix`() {
        val g = QuantumGate.I
        assertEquals("I", g.name)
        assertMatrixEquals(matrixOf(listOf(ONE, ZERO), listOf(ZERO, ONE)), g.m())
    }

    @Test
    fun `X matrix`() {
        val g = QuantumGate.X
        assertEquals("X", g.name)
        assertMatrixEquals(matrixOf(listOf(ZERO, ONE), listOf(ONE, ZERO)), g.m())
    }

    @Test
    fun `Y matrix`() {
        val g = QuantumGate.Y
        assertEquals("Y", g.name)
        assertMatrixEquals(matrixOf(listOf(ZERO, MINUS_IMAG), listOf(IMAG, ZERO)), g.m())
    }

    @Test
    fun `Z matrix`() {
        val g = QuantumGate.Z
        assertEquals("Z", g.name)
        assertMatrixEquals(matrixOf(listOf(ONE, ZERO), listOf(ZERO, MINUS_ONE)), g.m())
    }

    @Test
    fun `H matrix`() {
        val g = QuantumGate.H
        val s = Complex(INV_SQRT2)
        assertEquals("H", g.name)
        assertMatrixEquals(
            matrixOf(listOf(s, s), listOf(s, Complex(-INV_SQRT2))),
            g.m()
        )
    }

    @Test
    fun `S matrix`() {
        val g = QuantumGate.S
        assertEquals("S", g.name)
        assertMatrixEquals(matrixOf(listOf(ONE, ZERO), listOf(ZERO, IMAG)), g.m())
    }

    @Test
    fun `T matrix`() {
        val g = QuantumGate.T
        assertEquals("T", g.name)
        assertMatrixEquals(
            matrixOf(listOf(ONE, ZERO), listOf(ZERO, Complex(INV_SQRT2, INV_SQRT2))),
            g.m()
        )
    }

    @Test
    fun `Measurement matrix is identity`() {
        val g = QuantumGate.Measurement
        assertEquals("M", g.name)
        assertMatrixEquals(matrixOf(listOf(ONE, ZERO), listOf(ZERO, ONE)), g.m())
    }

    // ---------- phaseShift ----------

    @Test
    fun `phaseShift matrix and name`() {
        val theta = 0.75
        val g = QuantumGate.phaseShift(theta)
        assertEquals("Phase($theta)", g.name)
        assertMatrixEquals(
            matrixOf(listOf(ONE, ZERO), listOf(ZERO, Complex.fromPolar(1.0, theta))),
            g.m()
        )
    }

    @Test
    fun `phaseShift zero is identity`() {
        assertMatrixEquals(QuantumGate.I.m(), QuantumGate.phaseShift(0.0).m())
    }

    @Test
    fun `phaseShift pi equals Z`() {
        assertMatrixEquals(QuantumGate.Z.m(), QuantumGate.phaseShift(PI).m())
    }

    @Test
    fun `phaseShift pi over 2 equals S`() {
        assertMatrixEquals(QuantumGate.S.m(), QuantumGate.phaseShift(PI / 2).m())
    }

    @Test
    fun `phaseShift pi over 4 equals T`() {
        assertMatrixEquals(QuantumGate.T.m(), QuantumGate.phaseShift(PI / 4).m())
    }

    @Test
    fun `phaseShift is periodic with 2 pi`() {
        assertMatrixEquals(QuantumGate.I.m(), QuantumGate.phaseShift(2 * PI).m())
    }

    @Test
    fun `phaseShift gates compose by adding angles`() {
        val a = QuantumGate.phaseShift(0.3)
        val b = QuantumGate.phaseShift(0.5)
        assertMatrixEquals(QuantumGate.phaseShift(0.8).m(), multiply(a.m(), b.m()))
    }

    @Test
    fun `phaseShift negative angle is the inverse`() {
        val p = QuantumGate.phaseShift(1.1)
        val inv = QuantumGate.phaseShift(-1.1)
        assertMatrixEquals(identityMatrix(2), multiply(p.m(), inv.m()))
    }

    // ---------- general properties ----------

    @Test
    fun `every gate is a 2x2 matrix`() {
        allGates.forEach { assertSquare(it.m(), 2) }
    }

    @Test
    fun `every gate is unitary`() {
        allGates.forEach { gate ->
            try {
                assertUnitary(gate.m())
            } catch (e: AssertionError) {
                throw AssertionError("Gate ${gate.name} is not unitary: ${e.message}", e)
            }
        }
    }

    @Test
    fun `X Y Z H are Hermitian and self inverse`() {
        listOf(QuantumGate.X, QuantumGate.Y, QuantumGate.Z, QuantumGate.H).forEach { g ->
            assertMatrixEquals(g.m(), dagger(g.m()))
            assertMatrixEquals(identityMatrix(2), multiply(g.m(), g.m()))
        }
    }

    @Test
    fun `S squared equals Z`() {
        assertMatrixEquals(QuantumGate.Z.m(), multiply(QuantumGate.S.m(), QuantumGate.S.m()))
    }

    @Test
    fun `T squared equals S`() {
        assertMatrixEquals(QuantumGate.S.m(), multiply(QuantumGate.T.m(), QuantumGate.T.m()))
    }

    @Test
    fun `T to the eighth is identity`() {
        var m = identityMatrix(2)
        repeat(8) { m = multiply(QuantumGate.T.m(), m) }
        assertMatrixEquals(identityMatrix(2), m)
    }

    @Test
    fun `XY equals iZ`() {
        val iZ = Array(2) { r -> Array(2) { c -> IMAG * QuantumGate.Z.m()[r][c] } }
        assertMatrixEquals(iZ, multiply(QuantumGate.X.m(), QuantumGate.Y.m()))
    }

    @Test
    fun `XY and YX anticommute`() {
        val xy = multiply(QuantumGate.X.m(), QuantumGate.Y.m())
        val yx = multiply(QuantumGate.Y.m(), QuantumGate.X.m())
        val negYx = Array(2) { r -> Array(2) { c -> yx[r][c] * -1.0 } }
        assertMatrixEquals(xy, negYx)
    }

    @Test
    fun `H conjugation swaps X and Z`() {
        val h = QuantumGate.H.m()
        assertMatrixEquals(QuantumGate.X.m(), multiply(multiply(h, QuantumGate.Z.m()), h))
        assertMatrixEquals(QuantumGate.Z.m(), multiply(multiply(h, QuantumGate.X.m()), h))
    }

    @Test
    fun `gates act correctly on basis vectors`() {
        val zero = basisVector(2, 0)
        val one = basisVector(2, 1)

        assertVectorEquals(one, applyMatrix(QuantumGate.X.m(), zero))
        assertVectorEquals(zero, applyMatrix(QuantumGate.X.m(), one))

        assertVectorEquals(arrayOf(ZERO, IMAG), applyMatrix(QuantumGate.Y.m(), zero))
        assertVectorEquals(arrayOf(MINUS_IMAG, ZERO), applyMatrix(QuantumGate.Y.m(), one))

        assertVectorEquals(zero, applyMatrix(QuantumGate.Z.m(), zero))
        assertVectorEquals(arrayOf(ZERO, MINUS_ONE), applyMatrix(QuantumGate.Z.m(), one))
    }

    // ---------- toMatrix / matrix ----------

    @Test
    fun `toMatrix returns a deep copy`() {
        val g = QuantumGate.H
        val a = g.toMatrix()
        val b = g.toMatrix()
        assertNotSameInstances(a, b)
        assertTrue(a !== b)
        assertMatrixEquals(a, b)
    }

    @Test
    fun `mutating returned matrix does not affect gate`() {
        val g = QuantumGate.X
        val m = g.toMatrix()
        m[0][0] = Complex(42.0)
        assertEquals(ZERO, g.m00)
        assertMatrixEquals(matrixOf(listOf(ZERO, ONE), listOf(ONE, ZERO)), g.toMatrix())
    }

    @Test
    fun `matrix property equals toMatrix`() {
        allGates.forEach { assertMatrixEquals(it.toMatrix(), it.matrix) }
    }

    @Test
    fun `toMatrix reflects fields in correct positions`() {
        val g = QuantumGate("test", Complex(1.0, 2.0), Complex(3.0, 4.0), Complex(5.0, 6.0), Complex(7.0, 8.0))
        val m = g.toMatrix()
        assertEquals(Complex(1.0, 2.0), m[0][0])
        assertEquals(Complex(3.0, 4.0), m[0][1])
        assertEquals(Complex(5.0, 6.0), m[1][0])
        assertEquals(Complex(7.0, 8.0), m[1][1])
    }

    // ---------- data class behaviour ----------

    @Test
    fun `equality of single qubit gates`() {
        assertEquals(QuantumGate.X, QuantumGate.X)
        assertNotEquals(QuantumGate.X, QuantumGate.Y)
        assertEquals(QuantumGate.phaseShift(0.5), QuantumGate.phaseShift(0.5))
        assertEquals(QuantumGate.phaseShift(0.5).hashCode(), QuantumGate.phaseShift(0.5).hashCode())
    }

    @Test
    fun `I and Measurement have same matrix but different names`() {
        assertMatrixEquals(QuantumGate.I.m(), QuantumGate.Measurement.m())
        assertNotEquals(QuantumGate.I, QuantumGate.Measurement)
    }

    @Test
    fun `copy keeps the matrix`() {
        val copy = QuantumGate.H.copy(name = "H2")
        assertEquals("H2", copy.name)
        assertMatrixEquals(QuantumGate.H.m(), copy.m())
    }

    // ---------- top-level aliases ----------

    @Test
    fun `top level aliases point to companion gates`() {
        assertSame(QuantumGate.X, X)
        assertSame(QuantumGate.Y, Y)
        assertSame(QuantumGate.Z, Z)
        assertSame(QuantumGate.H, H)
        assertSame(QuantumGate.H, Hadamard)
        assertSame(QuantumGate.I, Identity)
        assertSame(QuantumGate.I, com.daedricknight.artem.quantumengine.ventiles.gate.I)
        assertSame(QuantumGate.Measurement, Measurement)
    }
}

// =====================================================================
// Two-qubit gates
// =====================================================================

class TwoQubitGateTest {

    private val allGates = listOf(TwoQubitQuantumGate.CNOT, TwoQubitQuantumGate.CZ, TwoQubitQuantumGate.SWAP)

    private fun permutation(vararg targets: Int): Matrix {
        // column c has a single 1 in row targets[c]
        return Array(targets.size) { r -> Array(targets.size) { c -> if (targets[c] == r) ONE else ZERO } }
    }

    // ---------- matrices ----------

    @Test
    fun `CNOT matrix and name`() {
        val g = TwoQubitQuantumGate.CNOT
        assertEquals("Cnot", g.name)
        assertMatrixEquals(permutation(0, 1, 3, 2), g.matrix)
    }

    @Test
    fun `CZ matrix and name`() {
        val g = TwoQubitQuantumGate.CZ
        assertEquals("Cz", g.name)
        val expected = identityMatrix(4).also { it[3][3] = MINUS_ONE }
        assertMatrixEquals(expected, g.matrix)
    }

    @Test
    fun `SWAP matrix and name`() {
        val g = TwoQubitQuantumGate.SWAP
        assertEquals("Swap", g.name)
        assertMatrixEquals(permutation(0, 2, 1, 3), g.matrix)
    }

    // ---------- general properties ----------

    @Test
    fun `all gates are 4x4`() {
        allGates.forEach { assertSquare(it.matrix, 4) }
    }

    @Test
    fun `all gates are unitary`() {
        allGates.forEach { assertUnitary(it.matrix) }
    }

    @Test
    fun `all gates are self inverse`() {
        allGates.forEach { assertMatrixEquals(identityMatrix(4), multiply(it.matrix, it.matrix)) }
    }

    @Test
    fun `all gates are Hermitian`() {
        allGates.forEach { assertMatrixEquals(it.matrix, dagger(it.matrix)) }
    }

    // ---------- behaviour on basis states (|control target|) ----------

    @Test
    fun `CNOT truth table`() {
        val m = TwoQubitQuantumGate.CNOT.matrix
        // |00> -> |00>, |01> -> |01>, |10> -> |11>, |11> -> |10>
        val expected = mapOf(0 to 0, 1 to 1, 2 to 3, 3 to 2)
        expected.forEach { (input, output) ->
            assertVectorEquals(basisVector(4, output), applyMatrix(m, basisVector(4, input)), "CNOT |$input>")
        }
    }

    @Test
    fun `CZ only flips sign of |11|`() {
        val m = TwoQubitQuantumGate.CZ.matrix
        for (i in 0..2) assertVectorEquals(basisVector(4, i), applyMatrix(m, basisVector(4, i)))
        assertVectorEquals(
            arrayOf(ZERO, ZERO, ZERO, MINUS_ONE),
            applyMatrix(m, basisVector(4, 3))
        )
    }

    @Test
    fun `SWAP exchanges |01| and |10| only`() {
        val m = TwoQubitQuantumGate.SWAP.matrix
        assertVectorEquals(basisVector(4, 0), applyMatrix(m, basisVector(4, 0)))
        assertVectorEquals(basisVector(4, 2), applyMatrix(m, basisVector(4, 1)))
        assertVectorEquals(basisVector(4, 1), applyMatrix(m, basisVector(4, 2)))
        assertVectorEquals(basisVector(4, 3), applyMatrix(m, basisVector(4, 3)))
    }

    @Test
    fun `SWAP equals three CNOTs with reversed middle control`() {
        val cnot = TwoQubitQuantumGate.CNOT.matrix
        // CNOT with control and target reversed
        val reversed = permutation(0, 3, 2, 1)
        val product = multiply(cnot, multiply(reversed, cnot))
        assertMatrixEquals(TwoQubitQuantumGate.SWAP.matrix, product)
    }

    @Test
    fun `CZ equals H on target conjugating CNOT`() {
        val hOnTarget = kron(identityMatrix(2), QuantumGate.H.m())
        val product = multiply(hOnTarget, multiply(TwoQubitQuantumGate.CNOT.matrix, hOnTarget))
        assertMatrixEquals(TwoQubitQuantumGate.CZ.matrix, product)
    }

    @Test
    fun `CNOT creates Bell state from H on control`() {
        val hOnControl = kron(QuantumGate.H.m(), identityMatrix(2))
        val state = applyMatrix(TwoQubitQuantumGate.CNOT.matrix, applyMatrix(hOnControl, basisVector(4, 0)))
        assertComplexEquals(Complex(INV_SQRT2), state[0])
        assertComplexEquals(ZERO, state[1])
        assertComplexEquals(ZERO, state[2])
        assertComplexEquals(Complex(INV_SQRT2), state[3])
    }

    // ---------- toMatrix ----------

    @Test
    fun `toMatrix returns deep copy with equal values`() {
        allGates.forEach { g ->
            val copy = g.toMatrix()
            assertMatrixEquals(g.matrix, copy)
            assertTrue(copy !== g.matrix)
            assertNotSameInstances(g.matrix, copy)
        }
    }

    @Test
    fun `mutating copy does not affect the gate`() {
        val g = TwoQubitQuantumGate.CNOT
        val copy = g.toMatrix()
        copy[0][0] = Complex(99.0)
        assertEquals(ONE, g.matrix[0][0])
    }

    // ---------- equals / hashCode ----------

    @Test
    fun `equal gates with distinct matrix instances`() {
        val a = TwoQubitQuantumGate("Cnot", TwoQubitQuantumGate.CNOT.toMatrix())
        assertEquals(TwoQubitQuantumGate.CNOT, a)
        assertEquals(TwoQubitQuantumGate.CNOT.hashCode(), a.hashCode())
    }

    @Test
    fun `equals is reflexive and rejects other types and null`() {
        val g = TwoQubitQuantumGate.CNOT
        assertTrue(g == g)
        assertTrue(!g.equals(null))
        assertTrue(!g.equals("Cnot"))
    }

    @Test
    fun `different name or matrix means not equal`() {
        assertNotEquals(TwoQubitQuantumGate.CNOT, TwoQubitQuantumGate.CZ)
        assertNotEquals(TwoQubitQuantumGate.CNOT, TwoQubitQuantumGate("Other", TwoQubitQuantumGate.CNOT.toMatrix()))
        assertNotEquals(TwoQubitQuantumGate.CNOT, TwoQubitQuantumGate("Cnot", TwoQubitQuantumGate.CZ.toMatrix()))
    }

    // ---------- top-level aliases ----------

    @Test
    fun `top level aliases`() {
        assertSame(TwoQubitQuantumGate.CNOT, Cnot)
        assertSame(TwoQubitQuantumGate.CZ, Cz)
        assertSame(TwoQubitQuantumGate.SWAP, Swap)
    }
}

// =====================================================================
// Three-qubit gates
// =====================================================================

class ThreeQubitGateTest {

    private val toffoli = ThreeQubitQuantumGate.TOFFOLI

    @Test
    fun `TOFFOLI name`() {
        assertEquals("CCnot", toffoli.name)
    }

    @Test
    fun `TOFFOLI is 8x8`() {
        assertSquare(toffoli.matrix, 8)
    }

    @Test
    fun `TOFFOLI is a permutation matrix that swaps only |110| and |111|`() {
        val expected = Array(8) { r ->
            Array(8) { c ->
                val target = when (c) { 6 -> 7; 7 -> 6; else -> c }
                if (target == r) ONE else ZERO
            }
        }
        assertMatrixEquals(expected, toffoli.matrix)
    }

    @Test
    fun `TOFFOLI truth table`() {
        for (input in 0..7) {
            val expected = when (input) { 6 -> 7; 7 -> 6; else -> input }
            assertVectorEquals(
                basisVector(8, expected),
                applyMatrix(toffoli.matrix, basisVector(8, input)),
                "Toffoli |${input.toString(2).padStart(3, '0')}>"
            )
        }
    }

    @Test
    fun `TOFFOLI is unitary, Hermitian and self inverse`() {
        assertUnitary(toffoli.matrix)
        assertMatrixEquals(toffoli.matrix, dagger(toffoli.matrix))
        assertMatrixEquals(identityMatrix(8), multiply(toffoli.matrix, toffoli.matrix))
    }

    @Test
    fun `TOFFOLI acts as AND on the target for classical inputs`() {
        // |a b 0> -> |a b (a AND b)>
        for (a in 0..1) for (b in 0..1) {
            val input = (a shl 2) or (b shl 1)
            val out = applyMatrix(toffoli.matrix, basisVector(8, input))
            val expectedIdx = input or (a and b)
            assertVectorEquals(basisVector(8, expectedIdx), out, "a=$a b=$b")
        }
    }

    @Test
    fun `toMatrix returns deep copy`() {
        val copy = toffoli.toMatrix()
        assertMatrixEquals(toffoli.matrix, copy)
        assertTrue(copy !== toffoli.matrix)
        assertNotSameInstances(toffoli.matrix, copy)
        copy[0][0] = Complex(5.0)
        assertEquals(ONE, toffoli.matrix[0][0])
    }

    @Test
    fun `equals and hashCode`() {
        val same = ThreeQubitQuantumGate("CCnot", toffoli.toMatrix())
        assertEquals(toffoli, same)
        assertEquals(toffoli.hashCode(), same.hashCode())
        assertNotEquals(toffoli, ThreeQubitQuantumGate("Other", toffoli.toMatrix()))
        val changed = toffoli.toMatrix().also { it[0][0] = ZERO }
        assertNotEquals(toffoli, ThreeQubitQuantumGate("CCnot", changed))
        assertTrue(toffoli == toffoli)
        assertTrue(!toffoli.equals(null))
        assertTrue(!toffoli.equals("CCnot"))
    }

    @Test
    fun `top level alias`() {
        assertSame(ThreeQubitQuantumGate.TOFFOLI, Toffoli)
    }
}
