package com.k1apps.backgammon.gamelogic

import kotlin.random.Random

class DiceImpl : Dice {
    override var enabled: Boolean = false
    override var number: Byte? = null
        private set

    override fun roll(): Byte {
        enabled = false
        number = Random.nextInt(1, 7).toByte()
        return number!!
    }

    companion object {
        fun build(dice: Dice): Dice {
            val diceImpl = DiceImpl()
            diceImpl.number = dice.number
            return diceImpl
        }
    }
}

interface Dice {
    var enabled: Boolean
    val number: Byte?
    fun roll(): Byte
}
