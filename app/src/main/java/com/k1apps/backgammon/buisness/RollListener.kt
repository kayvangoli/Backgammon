package com.k1apps.backgammon.buisness

interface DiceRollCallback {
    fun onThrewDice(player: Player, number: Byte)
}