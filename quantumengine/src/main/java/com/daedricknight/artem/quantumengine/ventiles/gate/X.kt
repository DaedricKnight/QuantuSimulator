
package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex


/**
 * The Pauli-X gate acts on a single qubit.
 * It is the quantum equivalent of the NOT gate for classical computers (with respect to the standard basis
 * |0>, |1> which distinguishes the Z-direction; in the sense that a measurement of the eigenvalue +1 corresponds to classical 1/true and -1 to 0/false.
 *
 */
class X(idx: Int) : SingleQubitGate(idx) {
    override var matrix: Array<Array<Complex>> = arrayOf(
        arrayOf(Complex.zero(), Complex.one()),
        arrayOf(Complex.one(), Complex.zero())
    )

    override var caption = "X"
}