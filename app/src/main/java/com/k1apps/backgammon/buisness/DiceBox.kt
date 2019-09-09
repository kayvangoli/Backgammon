package com.k1apps.backgammon.buisness

import kotlin.random.Random

class DiceBox private constructor() {
    companion object {
        val instance = DiceBox()
    }

    fun roll(): Pair<Byte, Byte> {
        val a: Byte = Random.nextInt(1, 7).toByte()
        val b: Byte = Random.nextInt(1, 7).toByte()
        return Pair(a, b)
    }
}