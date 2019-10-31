package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.DICE_RANGE
import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.REVERSE_HOME_RANGE
import com.k1apps.backgammon.gamelogic.*

class PlayerIsInRemovePieceStrategy :
    PlayerPiecesActionStrategy() {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val homeCellIndexRange: IntRange = if (list[0].moveType == MoveType.Normal) {
            NORMAL_HOME_RANGE
        } else {
            REVERSE_HOME_RANGE
        }
        if (isInRemovePieceState(homeCellIndexRange, list).not()) {
            throw ChooseStrategyException("State is not remove piece")
        }
        val headPieces = getHeadInGamePiecesFrom(list)
        diceBox.getAllUnUsedNumbers().forEach { number ->
            if (isNumberLargestAllLocations(number, headPieces)) {
                val piece = findPieceWithLargestLocation(headPieces)
                // TODO: 10/11/19 Kayvan: View Interaction for active piece
                diceBox.updateDiceStateWith(number)
            }
            headPieces.forEach { piece ->
                if (number <= piece.locationInMySide()) {
                    val pieceAfterMove = piece.pieceAfterMove(number)
                    if (pieceAfterMove != null) {
                        if (board.canMovePiece(piece, pieceAfterMove)) {
                            // TODO: 10/11/19 Kayvan: View Interaction for active piece
                            diceBox.updateDiceStateWith(number)
                        }
                    } else {
                        // TODO: 10/11/19 Kayvan: View Interaction for active piece
                        diceBox.updateDiceStateWith(number)
                    }
                }
            }
        }
    }

    override fun move(dice: Dice, piece: Piece, board: Board): Boolean {
        if (piece.state != PieceState.IN_GAME) {
            throw ChooseStrategyException("Choose remove strategy with state: ${piece.state}")
        }
        if (piece.locationInMySide() !in DICE_RANGE) {
            throw ChooseStrategyException("Player is not on remove but selected")
        }
        return board.move(piece, dice.number!!)
    }

    private fun findPieceWithLargestLocation(pieces: List<Piece>): Piece? {
        if (pieces.isEmpty()) {
            return null
        }
        var piece = pieces[0]
        pieces.forEach {
            if (it.locationInMySide() > piece.locationInMySide()) {
                piece = it
            }
        }
        return piece
    }

    private fun isNumberLargestAllLocations(number: Byte, pieces: List<Piece>): Boolean {
        pieces.forEach {
            if (number <= it.locationInMySide()) {
                return false
            }
        }
        return true
    }

    private fun isInRemovePieceState(homeCellIndexRange: IntRange, pieceList: ArrayList<Piece>)
            : Boolean {
        pieceList.forEach {
            if (it.state == PieceState.WON) return@forEach
            if (it.state == PieceState.DEAD) return false
            if (it.state == PieceState.IN_GAME && it.location !in homeCellIndexRange) return false
        }
        return true
    }
}