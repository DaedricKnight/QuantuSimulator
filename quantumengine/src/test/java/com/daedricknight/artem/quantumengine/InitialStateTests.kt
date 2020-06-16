/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Gluon Software
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
import com.daedricknight.artem.quantumengine.ventiles.gate.Identity
import com.daedricknight.artem.quantumengine.ventiles.gate.X
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class InitialStateTests : BaseGateTests() {
    @Test
    fun dontInitialize() {
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
    fun noStep() {
        val p = Program(0)
        val res = runProgram(p)
        Assert.assertEquals(0, res!!.qubits.size.toLong())
    }

    @Test
    fun initialize0() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        p.initializeQubit(0, 1.0)
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    @Test
    fun initialize1() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        p.initializeQubit(0, 0.0)
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(1, qubits[0].measure().toLong())
    }

    @Test
    fun initialize1Not() {
        val p = Program(1, Step(X(0)))
        p.initializeQubit(0, 0.0)
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
    }

    // 2 qubits, initally 1. Flip the first one, keep the second
    @Test
    fun TwoQubitinitialize1Not() {
        val p = Program(2, Step(X(0)))
        p.initializeQubit(0, 0.0)
        p.initializeQubit(1, 0.0)
        val res = runProgram(p)
        val qubits = res!!.qubits
        Assert.assertEquals(0, qubits[0].measure().toLong())
        Assert.assertEquals(1, qubits[1].measure().toLong())
    }

    // 1 qubit, 50% change 0 or 1, should at least be once 0 and once 1 in 100 tries
    @Test
    fun initializeFifty() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        val sq = Math.sqrt(.5)
        p.initializeQubit(0, sq)
        val cnt = 0
        var got0 = false
        var got1 = false
        var i = 0
        while (i < 100 && !(got0 && got1)) {
            val res = runProgram(p)
            val qubits = res!!.qubits
            val q0 = qubits[0].measure()
            if (q0 == 0) {
                got0 = true
            }
            if (q0 == 1) {
                got1 = true
            }
            i++
        }
        TestCase.assertTrue(got0)
        TestCase.assertTrue(got1)
    }

    // 1 qubit, 10% change 0, should be more 1 than 0 in 100 tries
    @Test
    fun initializeLowHigh() {
        val p = Program(
            1,
            Step(
                Identity(
                    0
                )
            )
        )
        val sq = Math.sqrt(.1)
        p.initializeQubit(0, sq)
        for (c in 0..9) {
            var cnt0 = 0
            var cnt1 = 1
            var i = 0
            while (i < 100) {
                val res = runProgram(p)
                val qubits = res!!.qubits
                val q0 = qubits[0].measure()
                if (q0 == 0) {
                    cnt0++
                }
                if (q0 == 1) {
                    cnt1++
                }
                i++
            }
            TestCase.assertTrue(cnt1 > cnt0)
        }
    }
}