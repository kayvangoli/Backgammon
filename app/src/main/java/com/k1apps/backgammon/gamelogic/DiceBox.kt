package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.DICE_RANGE

class DiceBoxImpl(override val dice1: Dice, override val dice2: Dice) : DiceBox {

    override var dice3: Dice? = null
        private set
    override var dice4: Dice? = null
        private set

    override fun roll() {
        Pair(dice1.roll(), dice2.roll())
        if (dice1.number == dice2.number) {
            dice3 = dice2.copy()
            dice4 = dice2.copy()
        } else {
            dice3 = null
            dice4 = null
        }
    }

    override fun updateDiceStateWith(number: Byte) {
        assert(number in DICE_RANGE)
        if (dice1.enabled.not() && dice1.number == number) {
            dice1.enabled = true
        } else if (dice2.enabled.not() && dice2.number == number) {
            dice2.enabled = true
        } else if (dice3 != null && dice3!!.enabled.not() && dice3!!.number == number) {
            dice3!!.enabled = true
        } else if (dice4 != null && dice4!!.enabled.not() && dice4!!.number == number) {
            dice4!!.enabled = true
        }
    }

    override fun isAtLeastOneDiceEnable(): Boolean {
        var enable = dice1.enabled || dice2.enabled
        dice3?.let {
            enable = it.enabled
        }
        dice4?.let {
            enable = it.enabled
        }
        return enable
    }

    override fun enable() {
        dice1.enabled = true
        dice2.enabled = true
        dice3?.enabled = true
        dice4?.enabled = true
    }

    override fun getAllUnUsedNumbers(): List<Byte> {
        val list = arrayListOf<Byte>()
        dice1.number?.let {
            if (dice1.used.not()) {
                list.add(it)
            }
        }
        dice2.number?.let {
            if (dice2.used.not()) {
                list.add(it)
            }
        }
        dice3?.number?.let {
            if (dice3!!.used.not()) {
                list.add(it)
            }
        }
        dice4?.number?.let {
            if (dice4!!.used.not()) {
                list.add(it)
            }
        }
        return list
    }

    override fun getActiveDiceWithNumber(number: Int): Dice? {
        dice1.number?.let {
            if (number.toByte() == it && dice1.isActive()) {
                return dice1
            }
        }
        dice2.number?.let {
            if (number.toByte() == it && dice2.isActive()) {
                return dice2
            }
        }
        dice3?.number?.let {
            if (number.toByte() == it && dice3!!.isActive()) {
                return dice3!!
            }
        }
        dice4?.number?.let {
            if (number.toByte() == it && dice4!!.isActive()) {
                return dice4!!
            }
        }
        return null
    }

    override fun getActiveDiceGreaterEqual(number: Int): Dice? {
        if (number !in DICE_RANGE) {
            throw DiceRangeException("$number is not in dice range($DICE_RANGE)")
        }
        for (num in number until 7) {
            val result = getActiveDiceWithNumber(num)
            if (result != null) {
                return result
            }
        }
        return null
    }

    override fun useDice(dice: Dice?) {
        var diceExist = false
        when {
            dice === dice1 -> diceExist = true
            dice === dice2 -> diceExist = true
            dice === dice3 -> diceExist = true
            dice === dice4 -> diceExist = true
        }
        if (diceExist.not()) {
            throw DiceException("Can not use dice while not exist")
        }
        dice1.used = true
    }

}

interface DiceBox {
    fun roll()
    val dice1: Dice
    val dice2: Dice
    val dice3: Dice?
    val dice4: Dice?
    fun updateDiceStateWith(number: Byte)
    fun isAtLeastOneDiceEnable(): Boolean
    fun enable()
    fun getAllUnUsedNumbers(): List<Byte>
    fun getActiveDiceWithNumber(number: Int): Dice?
    fun getActiveDiceGreaterEqual(number: Int): Dice?
    fun useDice(dice: Dice?)
}

