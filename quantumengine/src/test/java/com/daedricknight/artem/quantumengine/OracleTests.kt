/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, 2020, Gluon Software
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
package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.Complex
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.one
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.zero
import com.daedricknight.artem.quantumengine.ventiles.Program
import com.daedricknight.artem.quantumengine.ventiles.Step
import com.daedricknight.artem.quantumengine.ventiles.gate.Oracle
import org.junit.Assert
import org.junit.Test

class OracleTests : BaseGateTests() {
    @Test
    fun createOracleMatrix() {
        val matrix =
            Array(2) { Array(2) { Complex(0.0) } }
        matrix[0][0] = one()
        matrix[1][1] = one()
        matrix[0][1] = zero()
        matrix[1][0] = zero()
        val oracle = Oracle(matrix)
        Assert.assertNotNull(oracle)
    }

    // Oracle should replace null elements in matrix with Complex.ZERO
    @Test
    fun createDefaultOracle() {
        val matrix =
            Array(2) { Array(2) { Complex(0.0) } }
        matrix[0][0] = one()
        val oracle = Oracle(matrix)
        val omatrix: Array<Array<Complex>> = oracle.matrix
        Assert.assertNotNull(omatrix[1][1])
    }

    @Test
    fun identityOracle() {
        val matrix =
            Array(2) { Array(2) { Complex(0.0) } }
        matrix[0][0] = one()
        matrix[1][1] = one()
        matrix[0][1] = zero()
        matrix[1][0] = zero()
        val oracle = Oracle(matrix)
        val p = Program(1, Step(oracle))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun notOracle() {
        val matrix =
            Array(2) { Array(2) { Complex(0.0) } }
        matrix[0][1] = one()
        matrix[1][0] = one()
        val oracle = Oracle(matrix)
        val p = Program(1, Step(oracle))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }
}