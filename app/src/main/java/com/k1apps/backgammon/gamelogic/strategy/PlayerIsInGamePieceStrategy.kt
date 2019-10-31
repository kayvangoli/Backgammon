package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants
import com.k1apps.backgammon.gamelogic.*

class PlayerIsInGamePieceStrategy : PlayerPiecesActionStrategy() {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val headPieces = getHeadInGamePiecesFrom(list)
        headPieces.forEach { piece ->
            diceBox.getAllUnUsedNumbers().forEach { number ->
                val pieceAfterMove = piece.pieceAfterMove(number)
                if (pieceAfterMove != null && board.canMovePiece(piece, pieceAfterMove)) {
                    // TODO: 10/11/19 Kayvan: View Interaction for active piece
                    diceBox.updateDiceStateWith(number)
                }
            }
        }
    }

    override fun move(dice: Dice, piece: Piece, board: Board): Boolean {
        if (piece.state != PieceState.IN_GAME) {
            throw ChooseStrategyException("Choose in game strategy with state: ${piece.state}")
        }
        if (piece.pieceAfterMove(dice.number!!) == null) {
            throw ChooseStrategyException("Selected piece with number is out of board range")
        }
        return board.move(piece, dice.number!!)
    }
}