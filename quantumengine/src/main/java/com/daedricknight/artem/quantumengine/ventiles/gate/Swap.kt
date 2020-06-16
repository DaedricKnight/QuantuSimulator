package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex

/**
 *
 *The swap gate swaps two qubits. With respect to the basis |00>, |01, |10>, |11>.
 */
class Swap : TwoQubitGate {
    override var matrix: Array<Array<Complex>> = arrayOf(
        arrayOf(
            Complex.one(),
            Complex.zero(),
            Complex.zero(),
            Complex.zero()
        ),
        arrayOf(Complex.zero(), Complex.zero(), Complex.one(), Complex.one()),
        arrayOf(Complex.zero(), Complex.one(), Complex.zero(), Complex.zero()),
        arrayOf(Complex.zero(), Complex.zero(), Complex.zero(), Complex.one())
    )

    constructor() {}
    constructor(a: Int, b: Int) : super(a, b) {}

    override var caption = "S"
}