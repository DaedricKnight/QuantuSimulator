# Quantum Engine — gates and single-qubit state in Kotlin

A small, dependency-free Kotlin library that models the building blocks of a quantum simulator: complex probability amplitudes, a single-qubit state, and unitary gate matrices for one, two and three qubits.

The code is intentionally minimal and readable: every gate is just an explicit matrix of `Complex` numbers, so it is easy to inspect, test and extend.

## Features

- **`Complex`** — immutable complex number (`data class`) with `+`, `-`, `*` (complex and scalar), `absSquared()` and `Complex.fromPolar(r, θ)` (Euler's formula).
- **`QuantumState`** — a single qubit `|ψ⟩ = α|0⟩ + β|1⟩` with automatic normalization, `applyGate(...)`, and `probabilityZero()` / `probabilityOne()`.
- **Single-qubit gates** (`QuantumGate`): `I`, `X`, `Y`, `Z`, `H`, `S`, `T`, `Measurement`, and parametric `phaseShift(θ)`.
- **Two-qubit gates** (`TwoQubitQuantumGate`): `CNOT`, `CZ`, `SWAP` (4×4 matrices).
- **Three-qubit gates** (`ThreeQubitQuantumGate`): `TOFFOLI` / CCNOT (8×8 matrix).
- **`QGate` interface** plus `TwoQubitGate` / `ThreeQubitGate` base classes that attach a gate matrix to concrete qubit indexes in a circuit.

## Package layout

```
com.daedricknight.artem.quantumengine.ventiles
├── ComplexNumber.kt     Complex, QuantumState
└── gate
    ├── QuantumGate.kt   QuantumGate, TwoQubitQuantumGate, ThreeQubitQuantumGate,
    │                    top-level aliases (X, Y, Z, H, Cnot, Toffoli, ...)
    ├── QGate.kt         common interface of circuit-placed gates
    ├── TwoQubitGate.kt  abstract gate bound to two qubit indexes
    └── ThreeQubitGate.kt abstract gate bound to three qubit indexes
```

## Quick start

### Complex numbers

```kotlin
val a = Complex(1.0, 2.0)          // 1 + 2i
val b = Complex(3.0, 4.0)          // 3 + 4i

a + b                              // Complex(4.0, 6.0)
a * b                              // Complex(-5.0, 10.0)
a * 2.0                            // Complex(2.0, 4.0)
b.absSquared()                     // 25.0

Complex.fromPolar(1.0, Math.PI)    // ≈ Complex(-1.0, 0.0)  (e^{iπ} = -1)
```

### Evolving a qubit

```kotlin
val state = QuantumState()                  // |0⟩

state.applyGate(QuantumGate.H)              // (|0⟩ + |1⟩) / √2
state.probabilityZero()                     // ≈ 0.5
state.probabilityOne()                      // ≈ 0.5

// applyGate returns the same instance, so calls can be chained
val s = QuantumState()
    .applyGate(QuantumGate.H)
    .applyGate(QuantumGate.T)
    .applyGate(QuantumGate.H)

// Any 2x2 matrix can be used as a gate
val rz = QuantumGate.phaseShift(Math.PI / 3)
QuantumState().applyGate(QuantumGate.H).applyGate(rz)
```

Non-normalized input is normalized automatically:

```kotlin
val s = QuantumState(Complex(3.0), Complex(4.0))
s.alpha   // Complex(0.6, 0.0)
s.beta    // Complex(0.8, 0.0)
```

### Reading gate matrices

```kotlin
QuantumGate.X.matrix              // Array<Array<Complex>> 2x2, a fresh copy on every call
TwoQubitQuantumGate.CNOT.matrix   // 4x4
ThreeQubitQuantumGate.TOFFOLI.matrix // 8x8
```

`matrix` / `toMatrix()` always return a deep copy, so mutating the result never changes the gate.

### Placing a multi-qubit gate on qubits

Invoking a two- or three-qubit gate definition produces a `QGate` bound to concrete qubit indexes:

```kotlin
val cx = Cnot(first = 0, second = 1)   // control = qubit 0, target = qubit 1
cx.mainIndex          // 0
cx.secondIndex        // 1
cx.affectedIndexes    // [0, 1]
cx.highestAffectedIndex // 1
cx.group              // "TwoQubit"
cx.caption            // "Cnot"
cx.matrix             // the 4x4 CNOT matrix
```

### Defining your own gate

```kotlin
val sqrtX = QuantumGate(
    name = "SqrtX",
    m00 = Complex(0.5, 0.5), m01 = Complex(0.5, -0.5),
    m10 = Complex(0.5, -0.5), m11 = Complex(0.5, 0.5)
)
QuantumState().applyGate(sqrtX)
```

## Gate reference

### Single-qubit (`QuantumGate`)

| Gate | Access | Matrix | Effect |
|------|--------|--------|--------|
| I | `QuantumGate.I` | `[[1,0],[0,1]]` | Identity |
| X | `QuantumGate.X` | `[[0,1],[1,0]]` | Bit flip (NOT) |
| Y | `QuantumGate.Y` | `[[0,-i],[i,0]]` | Bit + phase flip |
| Z | `QuantumGate.Z` | `[[1,0],[0,-1]]` | Phase flip of `\|1⟩` |
| H | `QuantumGate.H` | `1/√2 · [[1,1],[1,-1]]` | Hadamard, creates superposition |
| S | `QuantumGate.S` | `[[1,0],[0,i]]` | Phase π/2 (`S² = Z`) |
| T | `QuantumGate.T` | `[[1,0],[0,e^{iπ/4}]]` | Phase π/4 (`T² = S`) |
| M | `QuantumGate.Measurement` | `[[1,0],[0,1]]` | Placeholder, identity matrix (see limitations) |
| Phase(θ) | `QuantumGate.phaseShift(θ)` | `[[1,0],[0,e^{iθ}]]` | Parametric phase shift |

### Two-qubit (`TwoQubitQuantumGate`)

| Gate | Access | Effect |
|------|--------|--------|
| CNOT | `TwoQubitQuantumGate.CNOT` | Flips the target when the control is `\|1⟩` |
| CZ | `TwoQubitQuantumGate.CZ` | Phase flip of `\|11⟩` |
| SWAP | `TwoQubitQuantumGate.SWAP` | Exchanges `\|01⟩` and `\|10⟩` |

### Three-qubit (`ThreeQubitQuantumGate`)

| Gate | Access | Effect |
|------|--------|--------|
| Toffoli (CCNOT) | `ThreeQubitQuantumGate.TOFFOLI` | Flips the third qubit iff both controls are `\|1⟩` |

### Top-level aliases

For convenience the gate package exposes short aliases:

`X`, `Y`, `Z`, `H`, `Hadamard`, `I`, `Identity`, `Measurement`, `Cnot`, `Cz`, `Swap`, `Toffoli`.

`S`, `T` and `phaseShift` are available only through `QuantumGate`.

## Conventions

- Matrices are **row-major**: `matrix[row][column]`, acting on column vectors.
- Multi-qubit basis states are ordered with the **first qubit as the most significant bit**: for two qubits the basis is `|00⟩, |01⟩, |10⟩, |11⟩`, so in `CNOT` the control is the first qubit and the target is the second.
- `QuantumState.applyGate` re-normalizes the state after every application (tolerance `1e-9`). This keeps floating-point drift in check, but also means a non-unitary gate will not be detected.
- `Complex` is a `data class`, so `==` compares components **exactly**, including the sign of zero (`0.0 != -0.0`). Use a tolerance when comparing computed results.

## Testing

Tests use `kotlin.test` and cover:

- all `Complex` operations and algebraic laws (commutativity, associativity, distributivity, `i² = -1`, Euler's identity);
- `QuantumState` normalization, probabilities, and the action of every gate;
- every gate matrix: exact entries, unitarity (`U†U = I`), self-inverse / Hermitian properties, and identities such as `S² = Z`, `T² = S`, `HZH = X`, `XY = iZ`, `SWAP = CNOT·CNOT'·CNOT`, `CZ = (I⊗H)·CNOT·(I⊗H)`;
- truth tables for `CNOT`, `CZ`, `SWAP` and `Toffoli`;
- `toMatrix()` deep-copy semantics, `equals` / `hashCode`, and top-level aliases.

```kotlin
// build.gradle.kts
dependencies {
    testImplementation(kotlin("test"))
}
```

```
./gradlew test
```

## Current limitations

- `QuantumState` models a **single qubit** only; there is no multi-qubit register or circuit simulator yet. Two- and three-qubit gates currently exist as matrices and circuit placeholders.
- `Measurement` is an identity matrix, not a real projective measurement.
- `ThreeQubitQuantumGate.invoke(...)` is not finished: the generated gate overrides `mainIndex` with `TODO("Not yet implemented")`, so reading it throws `NotImplementedError`. Because `ThreeQubitGate` reads `mainIndex` while initializing `affectedIndexes`, creating a gate via `Toffoli()` is expected to fail until this is implemented.
- In `ThreeQubitGate`, `affectedIndexes` is a property with an initializer, so it is computed once at construction and does not follow later `setAdditionalQubit` calls; `highestAffectedIndex` derives from it. `TwoQubitGate` handles this differently and recomputes the list on every access.
