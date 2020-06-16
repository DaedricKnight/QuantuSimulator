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
import com.daedricknight.artem.quantumengine.ventiles.gate.Toffoli
import com.daedricknight.artem.quantumengine.ventiles.gate.X
import org.junit.Assert
import org.junit.Test

/**
 *
 *
 */
class ThreeQubitGateTests : BaseGateTests() {
    @Test
    fun empty() {
    }

    @Test
    fun ToffoliGate0() {
        // |000> -> |000>
        val p =
            Program(3, Step(Toffoli(2, 1, 0)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGate1() {
        // |100> -> |100>
        val p = Program(
            3,
            Step(X(2)),
            Step(Toffoli(2, 1, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGate2() {
        // |110> -> |111>
        val p = Program(
            3,
            Step(X(2), X(1)),
            Step(Toffoli(2, 1, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGate3() {
        // |111> -> |110>
        val p = Program(
            3,
            Step(X(2), X(1), X(0)),
            Step(Toffoli(2, 1, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGate4() {
        // |001> -> |001>
        val p = Program(
            3,
            Step(X(0)),
            Step(Toffoli(2, 1, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGateR0() {
        // |000> -> |000>
        val p =
            Program(3, Step(Toffoli(0, 1, 2)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGateR1() {
        // |100> -> |100>
        val p = Program(
            3,
            Step(X(2)),
            Step(Toffoli(0, 1, 2))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGateR2() {
        // |011> -> |111>
        val p = Program(
            3,
            Step(X(0), X(1)),
            Step(Toffoli(0, 1, 2))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun ToffoliGateR3() {
        // |110> -> |111>
        val p = Program(
            4,
            Step(X(2), X(3)),
            Step(Toffoli(3, 2, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(4, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
        Assert.assertEquals(1, qubits[3].measure().toLong())
    }

    @Test
    fun ToffoliGateR4() {
        System.err.println("R4")
        // |1100> -> |1101>
        val p = Program(
            4,
            Step(X(2), X(3)),
            Step(Toffoli(3, 2, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(4, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
        Assert.assertEquals(1, qubits[3].measure().toLong())
    }

    @Test
    fun ToffoliGateS2() {
        System.err.println("S2")
        // |0101> -> |1101>
        val p = Program(
            4,
            Step(X(0), X(2)),
            Step(Toffoli(0, 2, 3))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(4, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
        Assert.assertEquals(1, qubits[3].measure().toLong())
    }
}