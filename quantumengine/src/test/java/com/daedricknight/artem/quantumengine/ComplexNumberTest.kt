package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.Complex
import com.daedricknight.artem.quantumengine.ventiles.QuantumState
import com.daedricknight.artem.quantumengine.ventiles.gate.QuantumGate
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertSame
import org.junit.Assert.assertNotEquals
import org.junit.Test
import kotlin.math.PI
import kotlin.math.sqrt

class ComplexTest {

    @Test
    fun `default imaginary part is zero`() {
        val z = Complex(3.0)
        assertEquals(3.0, z.real)
        assertEquals(0.0, z.imag)
    }

    @Test
    fun `constants ZERO and ONE`() {
        assertEquals(Complex(0.0, 0.0), Complex.ZERO)
        assertEquals(Complex(1.0, 0.0), Complex.ONE)
    }

    @Test
    fun `data class equality, copy and destructuring`() {
        val a = Complex(1.0, 2.0)
        assertEquals(Complex(1.0, 2.0), a)
        assertEquals(Complex(1.0, 2.0).hashCode(), a.hashCode())
        assertNotEquals(Complex(1.0, 2.5), a)
        assertEquals(Complex(5.0, 2.0), a.copy(real = 5.0))
        val (re, im) = a
        assertEquals(1.0, re)
        assertEquals(2.0, im)
    }

    // ---------- plus / minus ----------

    @Test
    fun `plus adds componentwise`() {
        assertEquals(Complex(4.0, 1.0), Complex(1.0, 3.0) + Complex(3.0, -2.0))
    }

    @Test
    fun `plus with ZERO is identity`() {
        val z = Complex(2.5, -7.0)
        assertEquals(z, z + Complex.ZERO)
        assertEquals(z, Complex.ZERO + z)
    }

    @Test
    fun `plus is commutative`() {
        val a = Complex(1.5, -2.0)
        val b = Complex(-0.5, 4.0)
        assertEquals(a + b, b + a)
    }

    @Test
    fun `minus subtracts componentwise`() {
        assertEquals(Complex(-2.0, 5.0), Complex(1.0, 3.0) - Complex(3.0, -2.0))
    }

    @Test
    fun `minus of itself is zero`() {
        val z = Complex(2.0, 3.0)
        assertEquals(Complex.ZERO, z - z)
    }

    @Test
    fun `minus is not commutative`() {
        val a = Complex(1.0, 1.0)
        val b = Complex(2.0, 3.0)
        assertEquals(a - b, Complex(-1.0, -2.0))
        assertEquals(b - a, Complex(1.0, 2.0))
    }

    // ---------- times (complex) ----------

    @Test
    fun `times follows (a+bi)(c+di) formula`() {
        // (1+2i)(3+4i) = 3 + 4i + 6i + 8i^2 = -5 + 10i
        assertEquals(Complex(-5.0, 10.0), Complex(1.0, 2.0) * Complex(3.0, 4.0))
    }

    @Test
    fun `i squared equals minus one`() {
        val i = Complex(0.0, 1.0)
        assertEquals(Complex(-1.0, 0.0), i * i)
    }

    @Test
    fun `times ONE is identity`() {
        val z = Complex(-3.0, 4.5)
        assertEquals(z, z * Complex.ONE)
        assertEquals(z, Complex.ONE * z)
    }

    @Test
    fun `times ZERO gives zero`() {
        val z = Complex(-3.0, 4.5)
        assertComplexEquals(Complex.ZERO, z * Complex.ZERO)
    }

    @Test
    fun `times is commutative`() {
        val a = Complex(1.0, 2.0)
        val b = Complex(-3.0, 0.5)
        assertComplexEquals(a * b, b * a)
    }

    @Test
    fun `times is associative`() {
        val a = Complex(1.0, 2.0)
        val b = Complex(-3.0, 0.5)
        val c = Complex(0.25, -4.0)
        assertComplexEquals((a * b) * c, a * (b * c))
    }

    @Test
    fun `times distributes over plus`() {
        val a = Complex(1.0, 2.0)
        val b = Complex(-3.0, 0.5)
        val c = Complex(0.25, -4.0)
        assertComplexEquals(a * (b + c), a * b + a * c)
    }

    @Test
    fun `multiplying by conjugate gives squared magnitude`() {
        val z = Complex(3.0, 4.0)
        assertComplexEquals(Complex(25.0, 0.0), z * conjugate(z))
    }

    // ---------- times (scalar) ----------

    @Test
    fun `times scalar scales both parts`() {
        assertEquals(Complex(3.0, -6.0), Complex(1.0, -2.0) * 3.0)
    }

    @Test
    fun `times scalar zero and one`() {
        val z = Complex(1.0, -2.0)
        assertEquals(z, z * 1.0)
        assertComplexEquals(Complex.ZERO, z * 0.0)
    }

    @Test
    fun `times negative scalar flips sign`() {
        assertEquals(Complex(-1.0, 2.0), Complex(1.0, -2.0) * -1.0)
    }

    @Test
    fun `scalar multiplication matches complex multiplication by real number`() {
        val z = Complex(1.5, -2.5)
        assertComplexEquals(z * Complex(2.0), z * 2.0)
    }

    // ---------- absSquared ----------

    @Test
    fun `absSquared of 3-4i is 25`() {
        assertEquals(25.0, Complex(3.0, 4.0).absSquared(), EPS)
    }

    @Test
    fun `absSquared of real and imaginary numbers`() {
        assertEquals(9.0, Complex(-3.0, 0.0).absSquared(), EPS)
        assertEquals(4.0, Complex(0.0, -2.0).absSquared(), EPS)
    }

    @Test
    fun `absSquared of ZERO and ONE`() {
        assertEquals(0.0, Complex.ZERO.absSquared(), EPS)
        assertEquals(1.0, Complex.ONE.absSquared(), EPS)
    }

    @Test
    fun `absSquared is multiplicative`() {
        val a = Complex(1.0, 2.0)
        val b = Complex(-3.0, 0.5)
        assertEquals(a.absSquared() * b.absSquared(), (a * b).absSquared(), 1e-9)
    }

    // ---------- fromPolar ----------

    @Test
    fun `fromPolar default radius is 1`() {
        assertComplexEquals(Complex(1.0, 0.0), Complex.fromPolar(theta = 0.0))
    }

    @Test
    fun `fromPolar at key angles`() {
        assertComplexEquals(Complex(0.0, 1.0), Complex.fromPolar(1.0, PI / 2))
        assertComplexEquals(Complex(-1.0, 0.0), Complex.fromPolar(1.0, PI))
        assertComplexEquals(Complex(0.0, -1.0), Complex.fromPolar(1.0, 3 * PI / 2))
        assertComplexEquals(Complex(1.0, 0.0), Complex.fromPolar(1.0, 2 * PI))
    }

    @Test
    fun `fromPolar at pi over 4`() {
        val s = 1.0 / sqrt(2.0)
        assertComplexEquals(Complex(s, s), Complex.fromPolar(1.0, PI / 4))
    }

    @Test
    fun `fromPolar respects radius`() {
        assertComplexEquals(Complex(0.0, 2.0), Complex.fromPolar(2.0, PI / 2))
        assertEquals(9.0, Complex.fromPolar(3.0, 1.234).absSquared(), 1e-9)
    }

    @Test
    fun `fromPolar with unit radius lies on unit circle for any angle`() {
        for (theta in listOf(-10.0, -1.0, 0.0, 0.3, 1.0, 5.0, 100.0)) {
            assertEquals("theta=$theta", 1.0, Complex.fromPolar(1.0, theta).absSquared(), EPS)
        }
    }

    @Test
    fun `fromPolar with zero radius is zero`() {
        assertComplexEquals(Complex.ZERO, Complex.fromPolar(0.0, 1.0))
    }

    @Test
    fun `fromPolar multiplication adds angles`() {
        val a = Complex.fromPolar(1.0, 0.4)
        val b = Complex.fromPolar(1.0, 0.7)
        assertComplexEquals(Complex.fromPolar(1.0, 1.1), a * b)
    }

    @Test
    fun `Euler identity e^(i pi) + 1 = 0`() {
        assertComplexEquals(Complex.ZERO, Complex.fromPolar(1.0, PI) + Complex.ONE)
    }
}

class QuantumStateTest {

    private val invSqrt2 = 1.0 / sqrt(2.0)

    private fun QuantumState.assertAmplitudes(alpha: Complex, beta: Complex) {
        assertComplexEquals(alpha, this.alpha, message = "alpha")
        assertComplexEquals(beta, this.beta, message = "beta")
    }

    // ---------- construction / normalization ----------

    @Test
    fun `default state is |0|`() {
        val s = QuantumState()
        s.assertAmplitudes(Complex.ONE, Complex.ZERO)
        assertEquals(1.0, s.probabilityZero(), EPS)
        assertEquals(0.0, s.probabilityOne(), EPS)
    }

    @Test
    fun `explicit |1| state`() {
        val s = QuantumState(Complex.ZERO, Complex.ONE)
        assertEquals(0.0, s.probabilityZero(), EPS)
        assertEquals(1.0, s.probabilityOne(), EPS)
    }

    @Test
    fun `unnormalized real input is normalized on init`() {
        val s = QuantumState(Complex(3.0), Complex(4.0))
        s.assertAmplitudes(Complex(0.6), Complex(0.8))
    }

    @Test
    fun `unnormalized complex input is normalized on init`() {
        val s = QuantumState(Complex(0.0, 2.0), Complex(0.0, 2.0))
        s.assertAmplitudes(Complex(0.0, invSqrt2), Complex(0.0, invSqrt2))
        assertEquals(1.0, s.probabilityZero() + s.probabilityOne(), EPS)
    }

    @Test
    fun `normalization preserves relative phase`() {
        val s = QuantumState(Complex(2.0), Complex(0.0, 2.0))
        s.assertAmplitudes(Complex(invSqrt2), Complex(0.0, invSqrt2))
    }

    @Test
    fun `already normalized state is not changed`() {
        val s = QuantumState(Complex(invSqrt2), Complex(0.0, invSqrt2))
        s.assertAmplitudes(Complex(invSqrt2), Complex(0.0, invSqrt2))
    }

    @Test
    fun `zero vector is left as is (no division by zero)`() {
        val s = QuantumState(Complex.ZERO, Complex.ZERO)
        assertEquals(Complex.ZERO, s.alpha)
        assertEquals(Complex.ZERO, s.beta)
        assertEquals(0.0, s.probabilityZero(), EPS)
        assertEquals(0.0, s.probabilityOne(), EPS)
    }

    // ---------- probabilities ----------

    @Test
    fun `probabilities are squared magnitudes`() {
        val s = QuantumState(Complex(0.6), Complex(0.0, 0.8))
        assertEquals(0.36, s.probabilityZero(), EPS)
        assertEquals(0.64, s.probabilityOne(), EPS)
    }

    // ---------- applyGate: return value ----------

    @Test
    fun `applyGate returns the same instance and mutates it`() {
        val s = QuantumState()
        val result = s.applyGate(QuantumGate.X)
        assertSame(s, result)
        s.assertAmplitudes(Complex.ZERO, Complex.ONE)
    }

    @Test
    fun `applyGate calls can be chained`() {
        val s = QuantumState().applyGate(QuantumGate.X).applyGate(QuantumGate.X)
        s.assertAmplitudes(Complex.ONE, Complex.ZERO)
    }

    // ---------- applyGate: individual gates ----------

    @Test
    fun `identity keeps the state`() {
        val s = QuantumState(Complex(0.6), Complex(0.0, 0.8)).applyGate(QuantumGate.I)
        s.assertAmplitudes(Complex(0.6), Complex(0.0, 0.8))
    }

    @Test
    fun `X flips |0| to |1| and back`() {
        val s = QuantumState().applyGate(QuantumGate.X)
        s.assertAmplitudes(Complex.ZERO, Complex.ONE)
        s.applyGate(QuantumGate.X)
        s.assertAmplitudes(Complex.ONE, Complex.ZERO)
    }

    @Test
    fun `Y maps |0| to i|1| and |1| to -i|0|`() {
        QuantumState().applyGate(QuantumGate.Y)
            .assertAmplitudes(Complex.ZERO, Complex(0.0, 1.0))
        QuantumState(Complex.ZERO, Complex.ONE).applyGate(QuantumGate.Y)
            .assertAmplitudes(Complex(0.0, -1.0), Complex.ZERO)
    }

    @Test
    fun `Z leaves |0| and negates |1|`() {
        QuantumState().applyGate(QuantumGate.Z)
            .assertAmplitudes(Complex.ONE, Complex.ZERO)
        QuantumState(Complex.ZERO, Complex.ONE).applyGate(QuantumGate.Z)
            .assertAmplitudes(Complex.ZERO, Complex(-1.0, 0.0))
    }

    @Test
    fun `H creates equal superposition from |0|`() {
        val s = QuantumState().applyGate(QuantumGate.H)
        s.assertAmplitudes(Complex(invSqrt2), Complex(invSqrt2))
        assertEquals(0.5, s.probabilityZero(), EPS)
        assertEquals(0.5, s.probabilityOne(), EPS)
    }

    @Test
    fun `H on |1| gives |-|`() {
        QuantumState(Complex.ZERO, Complex.ONE).applyGate(QuantumGate.H)
            .assertAmplitudes(Complex(invSqrt2), Complex(-invSqrt2))
    }

    @Test
    fun `H applied twice returns the original state`() {
        val s = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.H)
        s.assertAmplitudes(Complex.ONE, Complex.ZERO)
    }

    @Test
    fun `S on |+| gives (|0| + i|1|) over sqrt2`() {
        QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.S)
            .assertAmplitudes(Complex(invSqrt2), Complex(0.0, invSqrt2))
    }

    @Test
    fun `T on |+| gives phase pi over 4 on |1|`() {
        val s = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.T)
        s.assertAmplitudes(Complex(invSqrt2), Complex.fromPolar(invSqrt2, PI / 4))
    }

    @Test
    fun `Measurement gate does not change amplitudes`() {
        val s = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.Measurement)
        s.assertAmplitudes(Complex(invSqrt2), Complex(invSqrt2))
    }

    @Test
    fun `phaseShift changes phase but not probabilities`() {
        val s = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.phaseShift(1.234))
        assertEquals(0.5, s.probabilityZero(), EPS)
        assertEquals(0.5, s.probabilityOne(), EPS)
        s.assertAmplitudes(Complex(invSqrt2), Complex.fromPolar(invSqrt2, 1.234))
    }

    // ---------- applyGate: interference & identities ----------

    @Test
    fun `H Z H acts as X`() {
        val s = QuantumState()
            .applyGate(QuantumGate.H).applyGate(QuantumGate.Z).applyGate(QuantumGate.H)
        s.assertAmplitudes(Complex.ZERO, Complex.ONE)
    }

    @Test
    fun `H X H acts as Z`() {
        val s = QuantumState(Complex.ZERO, Complex.ONE)
            .applyGate(QuantumGate.H).applyGate(QuantumGate.X).applyGate(QuantumGate.H)
        s.assertAmplitudes(Complex.ZERO, Complex(-1.0, 0.0))
    }

    @Test
    fun `S applied twice equals Z`() {
        val viaS = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.S).applyGate(QuantumGate.S)
        val viaZ = QuantumState().applyGate(QuantumGate.H).applyGate(QuantumGate.Z)
        viaS.assertAmplitudes(viaZ.alpha, viaZ.beta)
    }

    @Test
    fun `T applied four times equals Z`() {
        val s = QuantumState().applyGate(QuantumGate.H)
        repeat(4) { s.applyGate(QuantumGate.T) }
        s.assertAmplitudes(Complex(invSqrt2), Complex(-invSqrt2))
    }

    @Test
    fun `norm is preserved after a long sequence of gates`() {
        val s = QuantumState()
        val gates = listOf(
            QuantumGate.H, QuantumGate.T, QuantumGate.S, QuantumGate.Y,
            QuantumGate.H, QuantumGate.X, QuantumGate.phaseShift(0.77), QuantumGate.Z
        )
        repeat(50) { gates.forEach { g -> s.applyGate(g) } }
        assertEquals(1.0, s.probabilityZero() + s.probabilityOne(), 1e-9)
    }

    @Test
    fun `non unitary gate is renormalized after application`() {
        val doubleIdentity = QuantumGate(
            "2I", Complex(2.0), Complex.ZERO, Complex.ZERO, Complex(2.0)
        )
        val s = QuantumState(Complex(0.6), Complex(0.8)).applyGate(doubleIdentity)
        s.assertAmplitudes(Complex(0.6), Complex(0.8))
    }
}
