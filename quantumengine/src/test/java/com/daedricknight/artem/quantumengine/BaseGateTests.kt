package com.daedricknight.artem.quantumengine

import com.daedricknight.artem.quantumengine.ventiles.Program
import com.daedricknight.artem.quantumengine.ventiles.QuantumExecutionEnvironment
import com.daedricknight.artem.quantumengine.ventiles.Result
import com.daedricknight.artem.quantumengine.ventiles.SimpleQuantumExecutionEnvironment

open class BaseGateTests {
    @Throws(RuntimeException::class)
    fun runProgram(program: Program?): Result? {
        val qee: QuantumExecutionEnvironment = SimpleQuantumExecutionEnvironment()
        return qee.runProgram(program)
    }

    companion object {
        /**
         * Flag for setting up the JavaFX, we only need to do this once for all tests.
         */
        private const val jfxIsSetup = false
    }
}