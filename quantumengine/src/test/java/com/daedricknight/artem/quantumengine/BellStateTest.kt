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
package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.Program
import com.daedricknight.artem.quantumengine.ventiles.Step
import com.daedricknight.artem.quantumengine.ventiles.gate.Cnot
import com.daedricknight.artem.quantumengine.ventiles.gate.Hadamard
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

/**
 *
 *
 */
class BellStateTest : BaseGateTests() {
    @Test
    fun empty() {
    }

    @Test
    fun hcnot01() {
        val p = Program(
            2,
            Step(Hadamard(0)),
            Step(Cnot(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        val q0 = qubits[0].measure()
        val q1 = qubits[1].measure()
        Assert.assertEquals(q0.toLong(), q1.toLong())
    }

    /**
     * When making multiple measurements on the same Result object, we
     * should be able to see different outcomes (0-0 or 1-1)
     */
    @Test
    fun multimeasurement() {
        val p = Program(
            2,
            Step(Hadamard(0)),
            Step(Cnot(0, 1))
        )
        val res = runProgram(p)
        var zeroCount = 0
        val RUNS = 100
        for (i in 0..99) {
            res!!.measureSystem()
            val qubits = res.qubits
            val q0 = qubits[0].measure()
            val q1 = qubits[1].measure()
            if (q0 == 0) zeroCount++
        }
        TestCase.assertTrue(zeroCount > 0)
        TestCase.assertTrue(zeroCount < RUNS)
    }

    /**
     * BellState with a third qubit that is sent through a H gate
     */
    @Test
    fun cnotH() {
        val p = Program(
            3,
            Step(Hadamard(0)),
            Step(Cnot(0, 1)),
            Step(Hadamard(2))
        )
        val res = runProgram(p)
        var zeroCount = 0
        var q2count0 = 0
        val RUNS = 100
        for (i in 0 until RUNS) {
            res!!.measureSystem()
            val qubits = res.qubits
            val q0 = qubits[0].measure()
            val q1 = qubits[1].measure()
            val q2 = qubits[2].measure()
            //            assertEquals(q0,q1);
            if (q0 == 0) zeroCount++
            if (q2 == 0) q2count0++
        }
        TestCase.assertTrue(zeroCount > 0)
        TestCase.assertTrue(zeroCount < RUNS)
        TestCase.assertTrue(q2count0 > 0)
        TestCase.assertTrue(q2count0 < RUNS)
    }
}