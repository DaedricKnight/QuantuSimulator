/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2018, 2019, Gluon Software
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
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.zero
import com.daedricknight.artem.quantumengine.ventiles.Computations.decomposeStep
import com.daedricknight.artem.quantumengine.ventiles.gate.Gate
import com.daedricknight.artem.quantumengine.ventiles.gate.Identity
import com.daedricknight.artem.quantumengine.ventiles.gate.ProbabilitiesGate
import com.daedricknight.artem.quantumengine.ventiles.gate.Swap
import java.util.*
import java.util.function.Consumer

/**
 *
 * Environment for execution simple qubit programs.
 */
class SimpleQuantumExecutionEnvironment : QuantumExecutionEnvironment {
    override fun runProgram(p: Program?): Result? {
        val nQubits = p!!.numberQubits
        val qubit = arrayOfNulls<Qubit>(nQubits)
        for (i in 0 until nQubits) {
            qubit[i] = Qubit()
        }
        val dim = 1 shl nQubits
        val initalpha = p.initialAlphas
        var probs = Array(dim) { Complex(0.0) }
        for (i in 0 until dim) {
            probs[i] = one()
            for (j in 0 until nQubits) {
                val pw = nQubits - j - 1
                val pt = 1 shl pw
                val div = i / pt
                val md = div % 2
                if (md == 0) {
                    probs[i] = probs[i]?.mult(initalpha[j])
                } else {
                    probs[i] = probs[i]!!
                        .mult(Math.sqrt(1 - initalpha[j] * initalpha[j]))
                }
            }
        }
        val steps =
            p.getSteps()
        var simpleSteps =
            p.decomposedSteps
        if (simpleSteps == null) {
            simpleSteps = ArrayList()
            for (step in steps) {
                simpleSteps.addAll(decomposeStep(step, nQubits))
            }
            p.decomposedSteps = simpleSteps
        }
        val result =
            Result(nQubits, steps.size)
        var cnt = 0
        if (simpleSteps.isEmpty()) {
            result.setIntermediateProbability(0, probs)
        }
        for (step in simpleSteps) {
            if (!step.getGates().isEmpty()) {
                cnt++
                probs = applyStep(step, probs, qubit)
                // printProbs(probs);
                val idx = step.complexStep
                if (idx > -1) {
                    result.setIntermediateProbability(idx, probs)
                }
            }
        }
        val qp = calculateQubitStatesFromVector(probs)
        for (i in 0 until nQubits) {
            qubit[i]!!.setProbability(qp[i])
        }
        result.measureSystem()
        p.resultBroken = result
        return result
    }

    override fun runProgram(
        p: Program?,
        resultBroken: Consumer<Result?>?
    ) {
        val t =
            Thread(Runnable { resultBroken!!.accept(runProgram(p)) })
        t.start()
    }

    private fun printProbs(p: Array<Complex>) {
        for (i in p.indices) {
            System.err.println("Probabiliy[" + i + "]: " + p[i])
        }
    }

    private fun decomposeSteps(steps: List<Step>): List<Step> {
        return steps
    }

    private fun applyStep(
        step: Step,
        vector: Array<Complex>,
        qubits: Array<Qubit?>
    ): Array<Complex> {
        val gates = step.getGates()
        if (!gates.isEmpty() && gates[0] is ProbabilitiesGate) {
            return vector
        }
        val a = calculateStepMatrix(gates, qubits.size)
        val result = Array(vector.size) { Complex(0.0) }//arrayOfNulls<Complex>(vector.size)
        if (a.size != result.size) {
            throw RuntimeException("Wrong length of matrix or probability vector")
        }
        for (i in vector.indices) {
            result[i] = zero()
            for (j in vector.indices) {
                result[i] = a[i][j]?.mult(vector[j]!!)?.let { result[i]?.add(it) }
            }
        }
        return result
    }

    private fun calculateStepMatrix(
        gates: List<Gate>,
        nQubits: Int
    ): Array<Array<Complex>> {
        return Computations.calculateStepMatrix(gates, nQubits)
    }

    // replaced by the similar function on Complex
    @Deprecated("")
    fun tensor(
        a: Array<Array<Complex>>,
        b: Array<Array<Complex>>
    ): Array<Array<Complex>> {
        val d1 = a.size
        val d2 = b.size
        val result = Array(d1 * d2) { Array(d1 * d2) { Complex(0.0) } }
        for (rowa in 0 until d1) {
            for (cola in 0 until d1) {
                for (rowb in 0 until d2) {
                    for (colb in 0 until d2) {
                        result[d2 * rowa + rowb][d2 * cola + colb] =
                            b[rowb][colb].let { a[rowa][cola].mult(it) }
                    }
                }
            }
        }
        return result
    }

    private fun calculateQubitStatesFromVector(vectorresult: Array<Complex>): DoubleArray {
        val nq = Math.round(
            Math.log(vectorresult.size.toDouble()) / Math.log(2.0)
        ).toInt()
        val answer = DoubleArray(nq)
        val ressize = 1 shl nq
        for (i in 0 until nq) {
            val div = 1 shl i
            for (j in 0 until ressize) {
                val p1 = j / div
                if (p1 % 2 == 1) {
                    answer[i] = answer[i] + vectorresult[j]!!.sqr()
                }
            }
        }
        return answer
    }

    fun createPermutationMatrix(
        first: Int,
        second: Int,
        n: Int
    ): Array<Array<Complex>> {
        val swapMatrix: Array<Array<Complex>> = Swap().matrix
        val iMatrix =
            Identity().matrix
        var answer = iMatrix
        var i = 1
        if (first == 0) {
            answer = swapMatrix
            i++
        }
        while (i < n) {
            answer = if (i == first) {
                i++
                tensor(answer, swapMatrix)
            } else {
                tensor(answer, iMatrix)
            }
            i++
        }
        return answer
    }

    private fun printMatrix(a: Array<Array<Complex>>) {
        for (i in a.indices) {
            val sb = StringBuilder()
            for (j in 0 until a[i].size) {
                sb.append(a[i][j]).append("    ")
            }
            System.err.println("m[$i]: $sb")
        }
    }
}