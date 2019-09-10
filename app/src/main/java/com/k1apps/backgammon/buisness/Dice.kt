package com.k1apps.backgammon.buisness

import kotlin.random.Random

class DiceImpl: Dice {
    override fun roll(): Byte {
        return Random.nextInt(1, 7).toByte()
    }
}

interface Dice{
    fun roll(): Byte
}
