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
package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Block
import com.daedricknight.artem.quantumengine.ventiles.Complex

/**
 *
 * A Gate describes an operation on one or more qubits.
 * Create a block
 * @param block
 * @param idx
 *
 */
class BlockGate(private val block: Block, private val idx: Int) : Gate {

    //To change body of generated methods, choose Tools | Templates.
    override var mainQubitIndex: Int
        get() = idx
        set(idx) {
            throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
        }

    override fun setAdditionalQubit(idx: Int, cnt: Int) {
        throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
    }

    override val affectedQubitIndexes: List<Int>
        get() = (idx..idx + block.nQubits).toList()//IntStream.range(idx, idx + block.nQubits).boxed().collect(Collectors.toList())

    override val highestAffectedQubitIndex: Int
        get() = block.nQubits + idx - 1

    override val caption: String
        get() = "B"

    override val name: String
        get() = "BlockGate"

    override val group: String
        get() = "BlockGroup"

    override val matrix: Array<Array<Complex>>
        get() = block.getMatrix()!!

    val size: Int
        get() = block.nQubits

    override fun toString(): String {
        return "Gate for block $block"
    }

}