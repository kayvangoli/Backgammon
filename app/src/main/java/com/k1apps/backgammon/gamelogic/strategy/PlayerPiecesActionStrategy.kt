package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.BOARD_LOCATION_RANGE
import com.k1apps.backgammon.gamelogic.Board
import com.k1apps.backgammon.gamelogic.DiceBox
import com.k1apps.backgammon.gamelogic.Piece
import com.k1apps.backgammon.gamelogic.PieceState

abstract class PlayerPiecesActionStrategy {
    abstract fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board)

    protected fun getHeadInGamePiecesFrom(list: ArrayList<Piece>): List<Piece> {
        val result = arrayListOf<Piece>()
        for (item in BOARD_LOCATION_RANGE) {
            loop@ for (piece in list) {
                if (item == piece.location && piece.state == PieceState.IN_GAME) {
                    result.add(piece)
                    break@loop
                }
            }
        }
        return result
    }
}