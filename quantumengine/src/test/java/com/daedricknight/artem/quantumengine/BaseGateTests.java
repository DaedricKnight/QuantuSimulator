package com.daedricknight.artem.quantumengine;


import com.daedricknight.artem.quantumengine.ventiles.Program;
import com.daedricknight.artem.quantumengine.ventiles.QuantumExecutionEnvironment;
import com.daedricknight.artem.quantumengine.ventiles.Result;
import com.daedricknight.artem.quantumengine.ventiles.SimpleQuantumExecutionEnvironment;

public class BaseGateTests {

    /**
     * Flag for setting up the JavaFX, we only need to do this once for all tests.
     */
    private static boolean jfxIsSetup;

    public Result runProgram(Program program) throws RuntimeException {
        QuantumExecutionEnvironment qee = new SimpleQuantumExecutionEnvironment();
        return qee.runProgram(program);
    }


}
