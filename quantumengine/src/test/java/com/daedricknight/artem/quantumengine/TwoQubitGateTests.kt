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
import org.junit.Assert
import org.junit.Test

/**
 *
 * Test for 2-qubits gates.
 */
class TwoQubitGateTests : BaseGateTests() {
    @Test
    fun empty() {
    }

    @Test
    fun doubleIGate() {
        val p = Program(
            2,
            Step(
                Identity(
                    0
                ), Identity(1)
            )
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun swapGate00() {
        val p = Program(
            2,
            Step(
                Identity(
                    0
                ), Identity(1)
            ),
            Step(Swap(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun swapGate01() {
        val p = Program(
            2,
            Step(
                Identity(
                    0
                ), X(1)
            ),
            Step(Swap(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun swapGate10() {
        val p = Program(
            2,
            Step(
                Identity(
                    1
                ), X(0)
            ),
            Step(Swap(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun swapGate11() {
        val p = Program(
            2,
            Step(X(0), X(1)),
            Step(Swap(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    @Test
    fun swapGate012() {
        val p = Program(
            3,
            Step(X(0)),
            Step(Swap(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(0, qubits[2].measure().toLong())
    }

    @Test
    fun swapGate122() {
        val p = Program(
            3,
            Step(X(1)),
            Step(Swap(1, 2))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun swapGate022() {
        val p = Program(
            3,
            Step(X(0)),
            Step(Swap(0, 2))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun swapGate202() {
        val p = Program(
            3,
            Step(X(0)),
            Step(Swap(2, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun cnot01() {
        val p = Program(2, Step(Cnot(0, 1)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun cnotx01() {
        val p = Program(
            2,
            Step(X(0)),
            Step(Cnot(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    @Test
    fun cnotx10() {
        val p = Program(
            2,
            Step(X(1)),
            Step(Cnot(1, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    @Test
    fun cnotx02() {
        val p = Program(
            3,
            Step(X(0)),
            Step(Cnot(0, 2))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun cnotx20() {
        val p = Program(
            3,
            Step(X(2)),
            Step(Cnot(2, 0))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun cnotx21() {
        val p = Program(
            3,
            Step(X(2)),
            Step(Cnot(2, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(3, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
        Assert.assertEquals(1, qubits[2].measure().toLong())
    }

    @Test
    fun cz01() {
        val p = Program(2, Step(Cz(0, 1)))
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun czx01() {
        val p = Program(
            2,
            Step(X(0)),
            Step(Cz(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(0, qubits[1].measure().toLong())
    }

    @Test
    fun czxx01() {
        val p = Program(
            2,
            Step(X(0), X(1)),
            Step(Cz(0, 1))
        )
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(2, qubits.size.toLong())
        Assert.assertEquals(1, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    @Test
    fun IMcnot() {
        val p = Program(
            2,
            Step(
                Identity(
                    0
                )
            ),
            Step(Measurement(0)),
            Step(Cnot(0, 1))
        )
    }

    @Test
    fun IMcnot10() {
        val p = Program(
            2,
            Step(
                Identity(
                    0
                )
            ),
            Step(Measurement(0))
        )
    }
}