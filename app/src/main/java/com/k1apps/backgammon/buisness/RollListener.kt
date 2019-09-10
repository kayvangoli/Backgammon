package com.k1apps.backgammon.buisness

interface RollListener {
    fun onThrewDice(player: Player, number: Byte)
    fun onThrewDiceBox(player: Player, pair: Pair<Byte, Byte>)
}