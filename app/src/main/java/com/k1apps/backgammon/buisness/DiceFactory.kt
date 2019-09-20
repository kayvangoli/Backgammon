package com.k1apps.backgammon.buisness

object DiceFactory {
    fun createDice(dice: Dice): Dice {
        return DiceImpl.build(dice)
    }
}