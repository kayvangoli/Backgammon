package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants
import com.k1apps.backgammon.gamelogic.MoveType
import com.k1apps.backgammon.gamelogic.Piece
import com.k1apps.backgammon.gamelogic.PieceState

interface PlayerPiecesContextStrategy {
    fun getPlayerPiecesStrategy(pieces: ArrayList<Piece>): PlayerPiecesActionStrategy
}

class PlayerPiecesContextStrategyImpl(
    private val inGamePieceStrategy: PlayerPiecesActionStrategy,
    private val removePieceStrategy: PlayerPiecesActionStrategy,
    private val deadPieceStrategy: PlayerPiecesActionStrategy
) : PlayerPiecesContextStrategy {
    override fun getPlayerPiecesStrategy(pieces: ArrayList<Piece>): PlayerPiecesActionStrategy {
        when {
            haveDiedPiece(pieces) -> return deadPieceStrategy
            isInRemovePieceState(pieces) -> return removePieceStrategy
        }
        return inGamePieceStrategy
    }

    private fun haveDiedPiece(pieces: ArrayList<Piece>): Boolean {
        pieces.forEach {
            if (it.state == PieceState.DEAD) {
                return true
            }
        }
        return false
    }

    private fun isInRemovePieceState(pieceList: ArrayList<Piece>)
            : Boolean {
        val homeCellIndexRange: IntRange = if (pieceList[0].moveType == MoveType.Normal) {
            Constants.NORMAL_HOME_RANGE
        } else {
            Constants.REVERSE_HOME_RANGE
        }
        pieceList.forEach {
            if (it.state == PieceState.DEAD) return false
            if (it.state == PieceState.IN_GAME &&
                it.location !in homeCellIndexRange &&
                it.state != PieceState.WON
            ) return false
        }
        return true
    }
}