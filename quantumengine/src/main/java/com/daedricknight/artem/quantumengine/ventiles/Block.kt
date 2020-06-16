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

import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.mmul
import com.daedricknight.artem.quantumengine.ventiles.Computations.calculateStepMatrix
import com.daedricknight.artem.quantumengine.ventiles.Computations.decomposeStep
import com.daedricknight.artem.quantumengine.ventiles.gate.Gate
import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate

/**
 *  Class for validation and get matrix.
 *
 */
class Block(private val name: String, val nQubits: Int) {
    var steps: MutableList<Step> =
        ArrayList()
    private var matrixBlock: Array<Array<Complex>>? = null

    constructor(size: Int) : this("anonymous", size) {}

    fun addStep(step: Step) {
        steps.add(step)
        matrixBlock = null
    }

    private fun validateGate(gate: Gate) {
        gate.affectedQubitIndexes?.stream()
            ?.filter(Predicate { idx: Int? -> idx ?: 0 > nQubits - 1 })
            ?.forEachOrdered(
                Consumer { item: Int? ->
                    throw IllegalArgumentException(
                        "Can't add a gate with qubit index larger than block size"
                    )
                }
            )
    }

    fun getMatrix(): Array<Array<Complex>> {
        if (matrixBlock == null) {
            val simpleSteps: MutableList<Step?> =
                ArrayList()
            for (step in steps) {
                simpleSteps.addAll(decomposeStep(step, nQubits))
            }
            simpleSteps.reverse()
            for (step in simpleSteps) {
                val m = calculateStepMatrix(step!!.getGates(), nQubits)
                if (matrixBlock == null) {
                    matrixBlock = m
                } else {
                    matrixBlock = mmul(matrixBlock!!, m)
                }
            }
        }
        return matrixBlock!!
    }

    override fun toString(): String {
        return "Block named " + name + " at " + super.toString()
    }

}