package com.k1apps.backgammon.buisness

class DiceBoxImpl(override val dice1: Dice, override val dice2: Dice): DiceBox {

    override fun roll(): Pair<Byte, Byte> {
        return Pair(dice1.roll(), dice2.roll())
    }
}

interface DiceBox{
    fun roll(): Pair<Byte, Byte>
    val dice1: Dice
    val dice2: Dice
}

