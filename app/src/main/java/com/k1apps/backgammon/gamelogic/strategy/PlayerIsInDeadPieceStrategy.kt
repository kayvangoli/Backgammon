package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.gamelogic.*

class PlayerIsInDeadPieceStrategy:
    PlayerPiecesActionStrategy {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val deadPieceCount = deadPieceCount(list)
        if (deadPieceCount == 0) {
            throw ChooseStrategyException("There are no dead piece in list")
        }
        if (deadPieceCount == 1) {

        }
    }

    private fun deadPieceCount(list: ArrayList<Piece>): Int {
        var count = 0
        list.forEach {
            if (it.state == PieceState.DEAD) {
                count++
            }
        }
        return count
    }
}