package com.k1apps.backgammon.buisness

import java.util.ArrayList

class PlayerImpl : Player {
    override var dice: Dice? = null
    override var diceBox: DiceBox? = null
    override var pieceList: ArrayList<Piece>? = null
    override val rollListener: RollListener? = null

    override fun roll() {
        if (dice != null) {

        }
    }
}

interface Player {
    var pieceList: ArrayList<Piece>?
    var diceBox: DiceBox?
    var dice: Dice?
    val rollListener: RollListener?
    fun roll()
}
