package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.DiceStatus
import kotlin.random.Random

class DiceImpl(override val random: Random) : Dice {
    private var twice = false
        set(value) {
            status.setTwice(value)
            field = value
        }
    private val status by lazy {
        DiceStatus()
    }

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

    override fun isActive() = isRolled() && status.enable

    override fun enableWith(number: Byte): Boolean {
        if (isRolled().not()) {
            throw DiceException("Dice is not rolled!")
        }
        if (this.number != number) {
            return false
        }
        if (status.isFullEnabled()) {
            return false
        }
        status.enable = true
        return true
    }

    override fun use(): Boolean {
        if (isRolled().not()) {
            throw DiceException("Dice is not roll yet!")
        }
        if (status.enable.not()) {
            return false
        }
        status.enable = false
        if (twice) {
            twice = false
        } else {
            number = null
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
    fun enableWith(number: Byte): Boolean
    fun use(): Boolean
    fun roll(): Byte
    fun isActive(): Boolean
    fun twice()
}
