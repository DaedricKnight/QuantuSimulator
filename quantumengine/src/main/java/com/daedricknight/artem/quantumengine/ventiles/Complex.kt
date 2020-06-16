package com.daedricknight.artem.quantumengine.ventiles

import kotlin.math.sqrt

/**
 * Create a complex number with a real and imaginary component
 * @param r the real component
 * @param i the imaginary component
 */
data class Complex(var real: Double, var imagine: Double = 0.0) {

    companion object {
        private val hv = 1.0 / sqrt(2.0)

        fun zero() = Complex(0.0, 0.0)

        fun one() = Complex(1.0, 0.0)

        fun imagine() = Complex(0.0, 1.0)

        fun hc() = Complex(this.hv, 0.0)

        fun hcn() = Complex(-this.hv, 0.0)

        fun tensor(
            a: Array<Array<Complex>>,
            b: Array<Array<Complex>>
        ): Array<Array<Complex>> {
            val d1 = a.size
            val d2 = b.size
            val tempResult = Array(d1 * d2) { Array(d1 * d2) { Complex(0.0) } }
            val result = tempResult
            for (rowa in 0 until d1) {
                for (cola in 0 until d1) {
                    for (rowb in 0 until d2) {
                        for (colb in 0 until d2) {
                            result[d2 * rowa + rowb][d2 * cola + colb] =
                                b[rowb][colb].let { a[rowa][cola].mult(it) }
                        }
                    }
                }
            }
            return result
        }

        fun mmul(
            a: Array<Array<Complex>>,
            b: Array<Array<Complex>>
        ): Array<Array<Complex>> {
            val arow = a.size
            val acol: Int = a[0].size
            val brow = b.size
            val bcol: Int = b[0].size
            if (acol != brow) throw RuntimeException("#cols a $acol != #rows b $brow")
            val answer = Array(1) { Array(2) { Complex(0.0) } }
            for (i in 0 until arow) {
                for (j in 0 until bcol) {
                    var el: Complex = zero()
                    for (k in 0 until acol) {
                        el = b[k][j].let { a[i][k].mult(it).let { el.add(it) } }
                    }
                    answer[i][j] = el
                }
            }
            return answer
        }
    }
    fun sqr() = this.real * this.real + this.imagine * this.imagine
}