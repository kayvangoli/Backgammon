package com.k1apps.backgammon.buisness

import kotlin.random.Random

class DiceImpl : Dice {
    override var enabled: Boolean = false
    override var number: Byte? = null

    override fun roll(): Byte {
        enabled = false
        number = Random.nextInt(1, 7).toByte()
        return number!!
    }
}

interface Dice {
    var enabled: Boolean
    var number: Byte?
    fun roll(): Byte
}
