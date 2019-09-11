package com.k1apps.backgammon.buisness

import java.util.ArrayList

class PlayerImpl(override val diceRollCallback: DiceRollCallback) : Player {
    override var dice: Dice? = null
    override var diceBox: DiceBox? = null
    override var pieceList: ArrayList<Piece>? = null

    override fun roll() {
        if (dice != null) {

        }
    }

    override fun retakeDice() {
        dice = null
    }
}

interface Player {
    var pieceList: ArrayList<Piece>?
    var diceBox: DiceBox?
    var dice: Dice?
    val diceRollCallback: DiceRollCallback
    fun roll()
    fun retakeDice()
}
