package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.Complex
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

const val EPS = 1e-9

typealias Matrix = Array<Array<Complex>>

fun assertComplexEquals(expected: Complex, actual: Complex, eps: Double = EPS, message: String? = null) {
    val prefix = message?.let { "$it: " } ?: ""
    assertEquals("${prefix}real part (expected=$expected, actual=$actual)", expected.real, actual.real, eps, )
    assertEquals("${prefix}imag part (expected=$expected, actual=$actual)", expected.imag, actual.imag, eps)
}

fun assertMatrixEquals(expected: Matrix, actual: Matrix, eps: Double = EPS) {
    assertEquals("row count", expected.size, actual.size, )
    for (r in expected.indices) {
        assertEquals( "column count in row $r", expected[r].size, actual[r].size)
        for (c in expected[r].indices) {
            assertComplexEquals(expected[r][c], actual[r][c], eps, "[$r][$c]")
        }
    }
}

fun conjugate(z: Complex) = Complex(z.real, -z.imag)

fun identityMatrix(n: Int): Matrix =
    Array(n) { r -> Array(n) { c -> if (r == c) Complex.ONE else Complex.ZERO } }

fun dagger(m: Matrix): Matrix =
    Array(m[0].size) { r -> Array(m.size) { c -> conjugate(m[c][r]) } }

fun multiply(a: Matrix, b: Matrix): Matrix =
    Array(a.size) { r ->
        Array(b[0].size) { c ->
            var sum = Complex.ZERO
            for (k in b.indices) sum += a[r][k] * b[k][c]
            sum
        }
    }

fun kron(a: Matrix, b: Matrix): Matrix {
    val n = a.size
    val m = b.size
    return Array(n * m) { r ->
        Array(n * m) { c -> a[r / m][c / m] * b[r % m][c % m] }
    }
}

fun applyMatrix(m: Matrix, v: Array<Complex>): Array<Complex> =
    Array(m.size) { r ->
        var sum = Complex.ZERO
        for (c in v.indices) sum += m[r][c] * v[c]
        sum
    }

fun basisVector(size: Int, index: Int): Array<Complex> =
    Array(size) { if (it == index) Complex.ONE else Complex.ZERO }

fun assertUnitary(m: Matrix, eps: Double = EPS) {
    assertMatrixEquals(identityMatrix(m.size), multiply(dagger(m), m), eps)
    assertMatrixEquals(identityMatrix(m.size), multiply(m, dagger(m)), eps)
}

fun assertSquare(m: Matrix, size: Int) {
    assertEquals( "row count", size, m.size)
    m.forEachIndexed { i, row -> assertEquals( "column count in row $i",size, row.size) }
}

fun assertNotSameInstances(a: Matrix, b: Matrix) {
    for (r in a.indices) for (c in a[r].indices) {
        assertTrue("[$r][$c] must be a copy, not the same instance", a[r][c] !== b[r][c])
    }
}

fun assertVectorEquals(expected: Array<Complex>, actual: Array<Complex>, message: String? = null, eps: Double = EPS) {
    val prefix = message?.let { "$it: " } ?: ""
    assertEquals("${prefix}vector size", expected.size, actual.size)
    for (i in expected.indices) {
        assertComplexEquals(expected[i], actual[i], eps, "$prefix[$i]")
    }
}