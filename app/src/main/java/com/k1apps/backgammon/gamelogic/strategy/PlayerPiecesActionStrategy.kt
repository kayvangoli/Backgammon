package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.BOARD_LOCATION_RANGE
import com.k1apps.backgammon.gamelogic.*

abstract class PlayerPiecesActionStrategy {
    abstract fun updateDiceBoxStatus(diceBox: DiceBox, list: ArrayList<Piece>, board: Board)

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

    abstract fun move(dice: Dice, piece: Piece, board: Board): Boolean

    abstract fun findDice(StartCellNumber: Int?,
                          destinationCellNumber: Int?,
                          diceBox: DiceBox,
                          board: Board): Dice?
}