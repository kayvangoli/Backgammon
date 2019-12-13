package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.DICE_RANGE
import kotlin.random.Random

class DiceImpl(override val random: Random) : Dice {
    override var enabled: Boolean = false
    override var used: Boolean = false
    override var number: Byte? = null
        private set(value) {
            enabled = false
            used = false
            value?.let {
                if (value !in DICE_RANGE) {
                    throw DiceRangeException("$value is not in dice range($DICE_RANGE)")
                }
            }
            field = value
        }

    override fun roll(): Byte {
        number = random.nextInt(1, 7).toByte()
        return number!!
    }

    override fun isActive(): Boolean {
        if (enabled.not()) {
            return false
        }
        if (used) {
            return false
        }
        return true
    }

    override fun copy(): Dice? {
        val dice = DiceImpl(random)
        dice.number = number
        return dice
    }
}

interface Dice {
    val random: Random
    var enabled: Boolean
    var used: Boolean
    val number: Byte?
    fun roll(): Byte
    fun isActive(): Boolean
    fun copy(): Dice?
}
