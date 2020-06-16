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
package com.daedricknight.artem.quantumengine;


import com.daedricknight.artem.quantumengine.ventiles.Complex;
import com.daedricknight.artem.quantumengine.ventiles.Program;
import com.daedricknight.artem.quantumengine.ventiles.Qubit;
import com.daedricknight.artem.quantumengine.ventiles.Result;
import com.daedricknight.artem.quantumengine.ventiles.Step;
import com.daedricknight.artem.quantumengine.ventiles.gate.Oracle;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OracleTests extends BaseGateTests {

    @Test
    public void createOracleMatrix() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.Companion.one();
        matrix[1][1] = Complex.Companion.one();
        matrix[0][1] = Complex.Companion.zero();
        matrix[1][0] = Complex.Companion.zero();
        Oracle oracle = new Oracle(matrix);
        assertNotNull(oracle);
    }

    // Oracle should replace null elements in matrix with Complex.ZERO
    @Test
    public void createDefaultOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.Companion.one();
        Oracle oracle = new Oracle(matrix);
        Complex[][] omatrix = oracle.getMatrix();
        assertNotNull(omatrix[1][1]);
    }

    @Test
    public void identityOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][0] = Complex.Companion.one();
        matrix[1][1] = Complex.Companion.one();
        matrix[0][1] = Complex.Companion.zero();
        matrix[1][0] = Complex.Companion.zero();
        Oracle oracle = new Oracle(matrix);
        Program p = new Program(1, new Step(oracle));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(0, qubits[0].measure());
    }


    @Test
    public void notOracle() {
        Complex[][] matrix = new Complex[2][2];
        matrix[0][1] = Complex.Companion.one();
        matrix[1][0] = Complex.Companion.one();
        Oracle oracle = new Oracle(matrix);
        Program p = new Program(1, new Step(oracle));
        Result res = runProgram(p);
        Qubit[] qubits = res.getQubits();
        assertEquals(1, qubits[0].measure());
    }
}
