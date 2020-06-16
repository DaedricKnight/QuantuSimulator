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
package com.daedricknight.artem.quantumengine.ventiles

import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.one
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.tensor
import com.daedricknight.artem.quantumengine.ventiles.gate.*
import java.util.*

/**
 * Class for calculation.
 *
 */
object Computations {
    fun calculateStepMatrix(
        gates: List<Gate>,
        nQubits: Int
    ): Array<Array<Complex>> {
        var a =
            Array(1) { Array(1) { Complex(0.0) } }
        a[0][0] = one()
        var idx = nQubits - 1
        while (idx >= 0) {
            val cnt = idx
            val myGate = gates.stream()
                .filter { gate: Gate -> gate.highestAffectedQubitIndex == cnt }
                .findFirst()
                .orElse(Identity(idx))
            if (myGate is BlockGate) {
                val sqg = myGate
                a = tensor(a, sqg.matrix)
                idx = idx - sqg.size + 1
            }
            if (myGate is SingleQubitGate) {
                a = tensor(a, myGate.matrix)
            }
            if (myGate is TwoQubitGate) {
                a = tensor(a, myGate.matrix)
                idx--
            }
            if (myGate is ThreeQubitGate) {
                a = tensor(a, myGate.matrix)
                idx = idx - 2
            }
            if (myGate is PermutationGate) {
                a = tensor(a, myGate.matrix)
                idx = 0
            }
            if (myGate is Oracle) {
                a = myGate.matrix
                idx = 0
            }
            idx--
        }
        //  printMatrix(a);
        return a
    }

    /**
     * decompose a Step into steps that can be processed without permutations
     * @param s
     * @return
     */
    fun decomposeStep(
        s: Step,
        nqubit: Int
    ): List<Step> {
        val answer =
            ArrayList<Step>()
        answer.add(s)
        val gates: List<Gate>? = s.getGates()
        if (gates!!.isEmpty()) return answer
        val simple = gates.stream()
            .allMatch { g: Gate? -> g is SingleQubitGate }
        if (simple) return answer
        // if only 1 gate, which is an oracle, return as well
        if (gates.size == 1 && gates[0] is Oracle) return answer
        // at least one non-singlequbitgate
        val firstGates: MutableList<Gate> = ArrayList()
        for (gate in gates) {
            if (gate is ProbabilitiesGate) {
                s.setInformalStep(true)
                return answer
            }
            if (gate is BlockGate) {
                firstGates.add(gate)
            } else if (gate is SingleQubitGate) {
                firstGates.add(gate)
            } else if (gate is TwoQubitGate) {
                val tqg = gate
                val first = tqg.mainQubitIndex
                val second = tqg.secondQubitIndex
                if (first == second + 1) {
                    firstGates.add(gate)
                } else {
                    if (first == second) throw RuntimeException("Wrong gate, first == second for $gate")
                    if (first > second) {
                        val pg = PermutationGate(first - 1, second, nqubit)
                        val prePermutation =
                            Step(pg)
                        val postPermutation =
                            Step(pg)
                        answer.add(0, prePermutation)
                        answer.add(postPermutation)
                        postPermutation.complexStep = s.index
                        s.complexStep = -1
                    } else {
                        val pg = PermutationGate(first, second, nqubit)
                        val prePermutation =
                            Step(pg)
                        val prePermutationInv =
                            Step(pg)
                        val realStep: Int = s.index
                        s.complexStep = -1
                        answer.add(0, prePermutation)
                        answer.add(prePermutationInv)
                        val postPermutation =
                            Step()
                        val postPermutationInv =
                            Step()
                        if (first != second - 1) {
                            val pg2 = PermutationGate(second - 1, first, nqubit)
                            postPermutation.addGate(pg2)
                            postPermutationInv.addGate(pg2)
                            answer.add(1, postPermutation)
                            answer.add(3, postPermutationInv)
                        }
                        prePermutationInv.complexStep = realStep
                    }
                }
            } else if (gate is ThreeQubitGate) {
                val tqg = gate
                val first: Int = tqg.mainQubitThreeQubitGate
                val second = tqg.secondQubit
                val third = tqg.thirdQubit
                var sFirst = first
                var sSecond = second
                var sThird = third
                if (first == second + 1 && second == third + 1) {
                    firstGates.add(gate)
                } else {
                    var p0idx = 0
                    val maxs = Math.max(second, third)
                    if (first < maxs) {
                        val pg = PermutationGate(first, maxs, nqubit)
                        val prePermutation =
                            Step(pg)
                        val postPermutation =
                            Step(pg)
                        answer.add(p0idx, prePermutation)
                        answer.add(answer.size - p0idx, postPermutation)
                        p0idx++
                        postPermutation.complexStep = s.index
                        s.complexStep = -1
                        sFirst = maxs
                        if (second > third) {
                            sSecond = first
                        } else {
                            sThird = first
                        }
                    }
                    if (sSecond != sFirst - 1) {
                        val pg = PermutationGate(sFirst - 1, sSecond, nqubit)
                        val prePermutation =
                            Step(pg)
                        val postPermutation =
                            Step(pg)
                        answer.add(p0idx, prePermutation)
                        answer.add(answer.size - p0idx, postPermutation)
                        p0idx++
                        postPermutation.complexStep = s.index
                        s.complexStep = -1
                        sSecond = sFirst - 1
                    }
                    if (sThird != sFirst - 2) {
                        val pg = PermutationGate(sFirst - 2, sThird, nqubit)
                        val prePermutation =
                            Step(pg)
                        val postPermutation =
                            Step(pg)
                        answer.add(p0idx, prePermutation)
                        answer.add(answer.size - p0idx, postPermutation)
                        p0idx++
                        postPermutation.complexStep = s.index
                        s.complexStep = -1
                        sThird = sFirst - 2
                    }
                }
            } else {
                throw RuntimeException("Gate must be SingleQubit or TwoQubit")
            }
        }
        return answer
    }

    fun printMatrix(a: Array<Array<Complex>>) {
        for (i in a.indices) {
            val sb = StringBuilder()
            for (j in 0 until a[i].size) {
                sb.append(a[i][j]).append("    ")
            }
            println("m[$i]: $sb")
        }
    }
}