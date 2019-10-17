package com.k1apps.backgammon.gamelogic

import kotlin.random.Random

class DiceImpl(override val random: Random) : Dice {
    override var enabled: Boolean = false
    override var number: Byte? = null
        private set

    override fun roll(): Byte {
        enabled = false
        number = random.nextInt(1, 7).toByte()
        return number!!
    }

    companion object {
        fun build(dice: Dice): Dice {
            val diceImpl = DiceImpl(dice.random)
            diceImpl.number = dice.number
            return diceImpl
        }
    }
}

interface Dice {
    val random: Random
    var enabled: Boolean
    val number: Byte?
    fun roll(): Byte
}
