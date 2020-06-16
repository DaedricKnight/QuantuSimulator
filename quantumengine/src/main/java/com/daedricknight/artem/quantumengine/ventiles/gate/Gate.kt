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

/**
 *
 * A Gate describes an operation on one or more qubits.
 *
 */
interface Gate {

    /**
     * Return the index of the main qubit this gate acts upon
     * @return the index of the main qubit
     */
    /**
     * Set the main qubit where this gate operates on.
     * @param idx the index of the main qubit
     */
    var mainQubitIndex: Int
    fun setAdditionalQubit(idx: Int, cnt: Int)
    val affectedQubitIndexes: List<Int?>?

    /**
     * Get the highest index of the qubit that is affected by this gate
     * @return the index of the highest affected qubit
     */
    val highestAffectedQubitIndex: Int
    val caption: String?
    val name: String?
    val group: String?
    val matrix: Array<Array<Complex>>

    companion object {
        fun cnot(a: Int, b: Int): Gate? {
            return Cnot(a, b)
        }

        fun cz(a: Int, b: Int): Gate? {
            return Cz(a, b)
        }

        fun hadamard(idx: Int): Gate? {
            return Hadamard(idx)
        }

        fun identity(idx: Int): Gate? {
            return Identity(idx)
        }

        fun measurement(idx: Int): Gate? {
            return Measurement(idx)
        }

        fun oracle(idx: Int): Gate? {
            return Oracle(idx)
        }

        fun oracle(matrix: Array<Array<Complex>>): Gate? {
            return Oracle(matrix)
        }

        fun permutation(a: Int, b: Int, n: Int): Gate? {
            return PermutationGate(a, b, n)
        }

        fun probability(idx: Int): Gate? {
            return ProbabilitiesGate(idx)
        }

        fun swap(a: Int, b: Int): Gate? {
            return Swap(a, b)
        }

        fun toffoli(a: Int, b: Int, c: Int): Gate? {
            return Toffoli(a, b, c)
        }

        fun x(idx: Int): Gate? {
            return X(idx)
        }

        fun y(idx: Int): Gate? {
            return Y(idx)
        }

        fun z(idx: Int): Gate? {
            return Z(idx)
        }
    }
}