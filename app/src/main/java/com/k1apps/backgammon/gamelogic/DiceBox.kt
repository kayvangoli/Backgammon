package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.DICE_RANGE

class DiceBoxImpl(override val dice1: Dice, override val dice2: Dice) : DiceBox {

    override var dice3: Dice? = null
        private set
    override var dice4: Dice? = null
        private set

    override fun roll() {
        if (dice1.number == dice2.number) {
            dice3 = DiceFactory.createDice(dice2)
            dice4 = DiceFactory.createDice(dice2)
        } else {
            dice3 = null
            dice4 = null
        }
        Pair(dice1.roll(), dice2.roll())
    }

    override fun canUseDiceWith(number: Byte) {
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
}

interface DiceBox {
    fun roll()
    val dice1: Dice
    val dice2: Dice
    val dice3: Dice?
    val dice4: Dice?
    fun canUseDiceWith(number: Byte)
}

