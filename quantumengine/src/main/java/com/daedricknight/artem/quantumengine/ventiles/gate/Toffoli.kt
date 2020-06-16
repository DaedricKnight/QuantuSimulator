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
package com.daedricknight.artem.quantumengine.ventiles.gate

import com.daedricknight.artem.quantumengine.ventiles.Complex
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.one
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.zero

/**
 * The Toffoli gate, named after Tommaso Toffoli; also called CCNOT gate or Deutsch
 * D ( π / 2 ) gate; is a 3-bit gate, which is universal for classical computation but not for quantum computation
 */
class Toffoli : ThreeQubitGate {
    override var matrix: Array<Array<Complex>> = arrayOf(
        arrayOf(
            one(),
            zero(),
            zero(),
            zero(),
            zero(),
            zero(),
            zero(),
            zero()
        ),
        arrayOf(zero(),  one(), zero(), zero(), zero(), zero(), zero(), zero()),
        arrayOf(zero(), zero(),  one(), zero(), zero(), zero(), zero(), zero()),
        arrayOf(zero(), zero(), zero(),  one(), zero(), zero(), zero(), zero()),
        arrayOf(zero(), zero(), zero(), zero(),  one(), zero(), zero(), zero()),
        arrayOf(zero(), zero(), zero(), zero(), zero(),  one(), zero(), zero()),
        arrayOf(zero(), zero(), zero(), zero(), zero(), zero(), zero(),  one()),
        arrayOf(zero(), zero(), zero(), zero(), zero(), zero(),  one(), zero())
    )

    constructor() {}
    constructor(a: Int, b: Int, c: Int) : super(a, b, c) {}

    override var caption: String
        get() = "CCnot"
        set(caption) {
            super.caption = caption
        }

    override var mainQubitIndex: Int
        get() = 0
        set(mainQubitIndex) {}

}