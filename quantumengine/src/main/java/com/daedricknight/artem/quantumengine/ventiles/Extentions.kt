package com.daedricknight.artem.quantumengine.ventiles

/**
 * Extentions for complex calculations.
 *
 */

fun Complex.add(compl: Complex) =
    Complex(this.real + compl.real, this.imagine + compl.imagine)

/**
 * Multiply extention Double on Complex.
 *
 */
fun Complex.mult(n: Double) = Complex(this.real * n, this.imagine * n)

/**
 * Multiply extention Complex on Complex.
 *
 */
fun Complex.mult(compl: Complex) = Complex(
    this.real * compl.real - this.imagine * compl.imagine,
    this.real * compl.imagine + this.imagine * compl.real
)

/**
 * Take the root of the complex.
 *
 */
fun Complex.sqr() = this.real * this.real + this.imagine * this.imagine