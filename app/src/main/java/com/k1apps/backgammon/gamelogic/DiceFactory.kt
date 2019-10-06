package com.k1apps.backgammon.gamelogic

object DiceFactory {
    fun createDice(dice: Dice): Dice {
        return DiceImpl.build(dice)
    }
}