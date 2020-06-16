
package com.daedricknight.artem.quantumengine.ventiles

import java.util.function.Consumer

/**
 * Interface for program running.
 */
interface QuantumExecutionEnvironment {
    /**
     * Execute the Program <code>p</code>.
     * As a result of this method, a <code>Result</code> object is created
     * containing the probability vector for the total system. This probability
     * vector is well defined: every invocation of this method with the same
     * <code>Program p</code> will return a <code>Result</code> instance that has
     * the same probability vector.
     * The <code>Result</code> also contains a list of Qubits, each with a measuredValue.
     * Every invocation of this method, or of the <code>Result.measureSystem</code> method
     * may result in different values for <code>measuredValue</code> for the qubits.
     * @param p the provided <code>Program</code> to be executed.
     * @return the <code>Result</code> instance containing the probability vector for
     * the system, and a measurement for every qubit.
     */
    fun runProgram(prog: Program?): Result?

    fun runProgram(
        p: Program?,
        resultBroken: Consumer<Result?>?
    )
}