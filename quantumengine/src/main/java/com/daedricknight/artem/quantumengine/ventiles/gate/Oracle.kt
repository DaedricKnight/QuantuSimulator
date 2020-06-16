/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2020, Gluon Software
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
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.zero
import java.util.*

/**
 * Create an Oracle based on the provided matrix.
 * Elements in this matrix may be `null` in which case they will be
 * replaced by `Complex.ZERO`
 * @param matrix the matrix describing this Oracle. If elements in this matrix are `null`, they
 * will be replaced with `Complex.ZERO`.
 */

class Oracle : Gate {
    private var mainQubit = 0
    private val affected: MutableList<Int> = LinkedList()
    override lateinit var matrix: Array<Array<Complex>>
        private set
    override var caption = "Oracle"
    var qubits = 1
        private set

    constructor(i: Int) {
        mainQubit = i
    }

    constructor(matrix: Array<Array<Complex>>) {
        this.matrix = matrix
        sanitizeMatrix()
        qubits = (Math.log(matrix.size.toDouble()) / Math.log(2.0)).toInt()
        for (i in 0 until qubits) {
            setAdditionalQubit(i, i)
        }
    }

    override var mainQubitIndex: Int
        get() = mainQubit
        set(idx) {
            mainQubit = 0
        }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        affected.add(idx)
    }

    override val affectedQubitIndexes: List<Int>
        get() = affected

    override val highestAffectedQubitIndex: Int
        get() = Collections.max(affectedQubitIndexes)

    override val name: String
        get() = "Oracle"

    override val group: String
        get() = "Oracle"

    // replace null with Complex.ZERO
    private fun sanitizeMatrix() {
        val rows = matrix.size
        for (i in 0 until rows) {
            val row = matrix[i]
            for (j in row.indices) {
                if (matrix[i][j] == null) {
                    matrix[i][j] = zero()
                }
            }
        }
    }
}