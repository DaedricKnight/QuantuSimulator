/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, Gluon Software
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.one
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.zero
import java.util.*

/**
 * Permutation gate.
 *
 */
class PermutationGate(a: Int, b: Int, n: Int) : Gate {
    private val a: Int
    private val b: Int
    private val n: Int
    override val matrix: Array<Array<Complex>>
    private val affected: MutableList<Int> = LinkedList()

    //To change body of generated methods, choose Tools | Templates.
    override var mainQubitIndex: Int
        get() = a
        set(idx) {
            throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
        }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
    }

    override val affectedQubitIndexes: List<Int>
        get() = affected

    override val highestAffectedQubitIndex: Int
        get() = Collections.max(affected)

    override val caption: String
        get() = "P"

    override val name: String
        get() = "permutation gate"

    override val group: String
        get() = "permutation"

    override fun toString(): String {
        return "Perm $a, $b"
    }

    init {
        assert(a < n)
        assert(b < n)
        this.a = a
        this.b = b
        this.n = n
        val dim = 1 shl n
        matrix = Array(dim) { Array(dim) { Complex(0.0) } }
        val amask = 1 shl a
        val bmask = 1 shl b
        for (i in 0 until dim) {
            val x = (amask and i) / amask
            val y = (bmask and i) / bmask
            if (x == y) {
                for (j in 0 until dim) {
                    matrix[i][j] =
                        if (i == j) one() else zero()
                }
            } else {
                val flipped = i xor amask xor bmask
                for (j in 0 until dim) {
                    matrix[i][j] =
                        if (j == flipped) one() else zero()
                }
            }
        }
        for (i in 0 until n) {
            affected.add(i)
        }
    }
}