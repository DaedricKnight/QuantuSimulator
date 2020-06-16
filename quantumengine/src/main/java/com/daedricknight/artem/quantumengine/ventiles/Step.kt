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
package com.daedricknight.artem.quantumengine.ventiles

import com.daedricknight.artem.quantumengine.ventiles.gate.Gate
import java.util.*
import java.util.function.Predicate

/**
 *
 * A single Step in a quantum `Program`. In a step, a number
 * of Gates can be added, involving a number of qubits. However, in a single
 * step a qubit can be involved in at most one `Gate`. It is illegal
 * to declare 2 gates in a single step that operate on the same qubit.
 *
 */
class Step(
    /**
     * Return the name of this step. This is for descriptive information only, it has no impact on the
     * computations
     * @return the name of the step, if supplied by the user.
     */
    val name: String, vararg moreGates: Gate
) {
    private val gates = ArrayList<Gate>()
    var index = 0
        set(s) {
            field = s
            complexStep = s
        }
    var program: Program? = null
    var complexStep = -1 // if a complex step needs to broken into

    // simple steps, only one simple step can have this value to be the index of the complex step
    var isInformal = false
        private set

    constructor(vararg moreGates: Gate) : this("unknown", *moreGates) {}

    /**
     * Add gate to the list of gates for this step
     * @param gate gate to add
     * @throws IllegalArgumentException in case the supplied Gate operates on a qubit that is already
     * been operated on in this step
     */
    @Throws(IllegalArgumentException::class)
    fun addGate(gate: Gate) {
        verifyUnique(Objects.requireNonNull(gate))
        gates.add(gate)
    }

    /**
     * Adds the multiple Gates to the list of gates for this step
     * @param moreGates more gates
     * @throws IllegalArgumentException in case the supplied Gate operates on a qubit that is already
     * been operated on in this step
     */
    @Throws(IllegalArgumentException::class)
    fun addGates(vararg moreGates: Gate) {
        for (g in moreGates) {
            addGate(g)
        }
    }

    fun getGates(): List<Gate> {
        return Collections.unmodifiableList(gates)
    }

    fun setInformalStep(b: Boolean) {
        isInformal = b
    }

    private fun verifyUnique(gate: Gate) {
        for (g in gates) {
            val overlap = g.affectedQubitIndexes!!.stream()
                .filter(Predicate { o: Int? ->
                    gate.affectedQubitIndexes!!.contains(o)
                }).count()
            require(overlap <= 0) { "Adding gate that affects a qubit already involved in this step" }
        }
    }

    override fun toString(): String {
        return "Step with gates " + gates
    }

    init {
        addGates(*moreGates)
    }
}