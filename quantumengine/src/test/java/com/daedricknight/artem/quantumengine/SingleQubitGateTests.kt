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
import com.daedricknight.artem.quantumengine.ventiles.gate.*
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

/**
 *
 * Test for single qubits gates.
 */
class SingleQubitGateTests : BaseGateTests() {
    @Test
    fun empty() {
    }

    @Test
    fun simpleIGate() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun simpleXGate() {
        val p = Program(1, Step(X(0)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }

    @Test
    fun simpleIXGate() {
        val p = Program(2, Step(X(0)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }

    @Test
    fun simpleXIGate() {
        val p = Program(2, Step(X(1)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    @Test
    fun simpleXIIGate() {
        val p = Program(3, Step(X(2)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun simpleYGate() {
        val p = Program(1, Step(Y(0)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }

    @Test
    fun simpleZGate() {
        val p = Program(1, Step(Z(0)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun simpleHGate() {
        val results = IntArray(2)
        for (i in 0..99) {
            val p =
                Program(1, Step(Hadamard(0)))
            val res = runProgram(p)
            val qubits = res!!.qubits
            results[qubits[0].measure()]++
        }
        TestCase.assertTrue(results[0] > 10)
        TestCase.assertTrue(results[1] > 10)
    }

    @Test
    fun simpleTogetherGate() {
        val p = Program(
            4,
            Step(
                X(0),
                Y(1),
                Z(2),
                Identity(3)
            )
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[2].measure().toLong())
        Assert.assertEquals(0, qubits[3].measure().toLong())
    }

    @Test
    fun simpleIM() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun simpleXM() {
        val p = Program(
            1,
            Step(X(0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }

    @Test
    fun simpleXMH() {
        val p = Program(
            1,
            Step(X(0))
        )
        p.addStep(Step(Hadamard(0)))
    }
}