package com.k1apps.backgammon.buisness

class RefereeImpl(
    private val board: Board,
    private val diceDistributor: DiceDistributor
) : Referee {

    override fun start() {
        diceDistributor.start()
        board.initBoard()
    }

    override fun roll(playerType: PlayerType) {
        val pair = diceDistributor.whichPlayerHasDice()
        pair?.let {
            pair.second?.let {
                if (playerType == pair.first.playerType) {
                    pair.first.roll()
                } else if (playerType == it.playerType) {
                    pair.second!!.roll()
                }
                return
            }
            if (pair.first.playerType == playerType) {
                pair.first.roll()
            }
        }
    }
}

interface Referee {
    fun start()
    fun roll(playerType: PlayerType)
}

