package com.k1apps.backgammon.buisness

class RefereeImpl(
    private val board: Board,
    private val diceBox: DiceBox,
    private val player1: Player,
    private val player2: Player,
    private val diceDistributor: DiceDistributor
) : Referee {

    override fun start() {
        diceDistributor.start()
        board.initBoard()
        player1.dice = diceBox.dice1
        player2.dice = diceBox.dice2
    }
}

interface Referee {
    fun start()
}

