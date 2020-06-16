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

import com.daedricknight.artem.quantumengine.ventiles.gate.Cnot
import com.daedricknight.artem.quantumengine.ventiles.gate.Gate
import com.daedricknight.artem.quantumengine.ventiles.gate.Hadamard
import com.daedricknight.artem.quantumengine.ventiles.gate.Measurement
import java.util.*

/**
 *
 * A Quantum Program.
 * A Program contains a list of `Step`s that are executed sequentially
 * by a QuantumExecutionEnvironment.
 *
 */
class Program(
    val numberQubits: Int,
    vararg moreSteps: Step
) {
    var resultBroken: Result? = null
    val initialAlphas: DoubleArray
    private val steps =
        ArrayList<Step>()

    // cache decomposedSteps
    var decomposedSteps: List<Step>? =
        null

    /**
     * Initialize this qubit. The qubit will be in a state
     * \psi = \alpha |0 &gt; + \beta |1 &gt; .
     * Since \alpha^2 + \beta^2 should equals 1, only
     * \alpha is required.
     * @param idx the index of the qubit to be initialized
     * @param alpha the alpha value of the qubit state.
     * @throws IllegalArgumentException in case the index of the qubit is higher than the amount of qubits -1 .
     */
    fun initializeQubit(idx: Int, alpha: Double) {
        require(idx < numberQubits) {
            "Can not initialize qubit " +
                    idx + " since we have only " + numberQubits + " qubits."
        }
        initialAlphas[idx] = alpha
    }

    /**
     * Adds a step with one or more gates to the existing program.
     * In case the Step contains an operation that would put a measured qubit into a potential superposition
     * again, an IllegalArgumentException is thrown.
     * @param step the step to be added to the program
     */
    fun addStep(step: Step) {
        require(ensureMeasuresafe(Objects.requireNonNull(step))) { "Adding a superposition step to a measured qubit" }
        step.index = steps.size
        step.program = this
        steps.add(step)
        decomposedSteps = null
    }

    /**
     * Adds muliple steps with one or more gates to the existing program.
     * In case the Step contains an operation that would put a measured qubit into a potential superposition
     * again, an IllegalArgumentException is thrown.
     * @param moreSteps steps to be added to the program
     */
    fun addSteps(vararg moreSteps: Step) {
        for (step in moreSteps) {
            addStep(step)
        }
    }

    private fun ensureMeasuresafe(newStep: Step): Boolean {

        // determine which qubits might get superpositioned
        val mainQubits: MutableList<Int> = ArrayList()
        for (g in newStep.getGates()) {
            if (g is Hadamard) {
                mainQubits.add(g.mainQubitIndex)
            } else if (g is Cnot) {
                mainQubits.add(g.secondQubitIndex)
            }
        }
        for (step in getSteps()) {
            val match = step.getGates().stream()
                .filter { g: Gate? -> g is Measurement }
                .map(Gate::mainQubitIndex)
                .anyMatch { o: Int -> mainQubits.contains(o) }
            if (match) return false
        }
        return true
    }

    fun getSteps(): List<Step> {
        return steps
    }

    /**
     * Print info about this program to stdout
     */
    fun printInfo() {
        println("Info about Quantum Program")
        println("==========================")
        println("Number of qubits = " + numberQubits + ", number of steps = " + steps.size)
        for (step in steps) {
            println("Step: " + step.getGates())
        }
        println("==========================")
    }

    /**
     * Create a Quantum Program and indicate how many qubits will be involved.
     * By default, all qubits are initialized to the |0 &gt; state.
     * @param nQubits the amount of qubits that will be used in this program
     * @param moreSteps steps to add to the program
     */
    init {
        initialAlphas = DoubleArray(numberQubits)
        Arrays.fill(initialAlphas, 1.0)
        addSteps(*moreSteps)
    }
}