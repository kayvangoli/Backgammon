package com.k1apps.backgammon.buisness

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
}

interface DiceBox {
    fun roll()
    val dice1: Dice
    val dice2: Dice
    val dice3: Dice?
    val dice4: Dice?
}

