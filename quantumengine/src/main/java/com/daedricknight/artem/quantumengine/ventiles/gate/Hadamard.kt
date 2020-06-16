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

import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.hc
import com.daedricknight.artem.quantumengine.ventiles.Complex.Companion.hcn

/**
 *
 * The Hadamard gate acts on a single qubit.
 * It maps the basis state | 0 ⟩ {\displaystyle |0\rangle } |0\rangle to | 0 ⟩ + | 1 ⟩ 2 {\displaystyle {\frac {|0\rangle +|1\rangle }{\sqrt {2}}}} \frac{|0\rangle + |1\rangle}{\sqrt{2}} and | 1 ⟩ {\displaystyle |1\rangle } |1\rangle to | 0 ⟩ − | 1 ⟩ 2 {\displaystyle {\frac {|0\rangle -|1\rangle }{\sqrt {2}}}} \frac{|0\rangle - |1\rangle}{\sqrt{2}},
 * which means that a measurement will have equal probabilities to become 1 or 0 (i.e. creates a superposition).
 * It represents a rotation of π {\displaystyle \pi } \pi about the axis ( x ^ + z ^ ) / 2 {\displaystyle ({\hat {x}}+{\hat {z}})/{\sqrt {2}}} (\hat{x}+\hat{z})/\sqrt{2} at the Bloch sphere.
 */
class Hadamard(idx: Int) : SingleQubitGate(idx) {
    override var matrix = arrayOf(
        arrayOf(hc(), hc()), arrayOf(hc(), hcn())
    )

    override var caption: String
        get() = "H"
        set(caption) {
            super.caption = caption
        }

}