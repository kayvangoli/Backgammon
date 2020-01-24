package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.DICE_RANGE
import kotlin.random.Random

class DiceImpl(override val random: Random) : Dice {
    private var enable = false
    private var twice = false

    override var number: Byte? = null
        private set

    override fun roll(): Byte {
        if (isRolled()) {
            throw DiceException("Roll dice twice!")
        }
        number = random.nextInt(1, 7).toByte()
        return number!!
    }

    private fun isRolled(): Boolean {
        return number != null
    }

    override fun isActive() = isRolled() && enable

    override fun enable() {
        if (isRolled().not()) {
            throw DiceException("Dice is not rolled!")
        }
        enable = true
    }

    override fun use(): Boolean {
        if (isRolled().not()) {
            throw DiceException("Dice is not roll yet!")
        }
        if (enable.not()) {
            return false
        }
        if (twice) {
            twice = false
        } else {
            number = null
            enable = false
        }
        return true
    }

    override fun twice() {
        if (isRolled().not()) {
            throw DiceException("Dice is not roll yet!")
        }
        twice = true
    }
}

interface Dice {
    val random: Random
    val number: Byte?
    fun enable()
    fun use(): Boolean
    fun roll(): Byte
    fun isActive(): Boolean
    fun twice()
}
