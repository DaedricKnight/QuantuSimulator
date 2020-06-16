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
import com.daedricknight.artem.quantumengine.ventiles.gate.Hadamard
import com.daedricknight.artem.quantumengine.ventiles.gate.Identity
import junit.framework.TestCase
import org.junit.Test

class SyntaxTests {
    @Test
    fun simpleIGate() {
        val p = Program(1)
        val s =
            Step(
                Identity(0)
            )
        var gotException = false
        try {
            s.addGate(Hadamard(0))
        } catch (ex: IllegalArgumentException) {
            gotException = true
        }
        TestCase.assertTrue(gotException)
    }

    @Test
    fun testNamedStep() {
        val s0 =
            Step("Hello!")
        val s1 =
            Step(
                "foo",
                Identity(0)
            )
        val s2 =
            Step(
                Identity(0),
                Identity(1)
            )
    }
}