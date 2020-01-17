package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.DICE_RANGE
import com.k1apps.backgammon.gamelogic.memento.Memento
import com.k1apps.backgammon.gamelogic.memento.Originator
import kotlin.random.Random

class DiceImpl(override val random: Random) : Dice {
    override var enabled: Boolean = false
    override var used: Boolean = false
    override var number: Byte? = null
        private set(value) {
            value?.let {
                if (value !in DICE_RANGE) {
                    throw DiceRangeException("$value is not in dice range($DICE_RANGE)")
                }
            }
            field = value
        }

    override fun roll(): Byte {
        if (used.not()) {
            throw DiceException("Can not roll unUsed dice!")
        }
        enabled = false
        used = false
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

    override fun createMemento(): Memento {
        return DiceMemento(enabled, used, number)
    }

    override fun restore(memento: Memento) {
        (memento as DiceMemento).let {
            number = it.number
            used = it.used
            enabled = it.enabled
        }
    }

    private data class DiceMemento(
        val enabled: Boolean = false,
        val used: Boolean,
        val number: Byte?
    ) : Memento
}

interface Dice : Originator {
    val random: Random
    var enabled: Boolean
    var used: Boolean
    val number: Byte?
    fun roll(): Byte
    fun isActive(): Boolean
    fun copy(): Dice?
}
