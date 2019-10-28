package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.gamelogic.Board
import com.k1apps.backgammon.gamelogic.DiceBox
import com.k1apps.backgammon.gamelogic.Piece

class PlayerIsInGamePieceStrategy : PlayerPiecesActionStrategy() {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val headPieces = getHeadInGamePiecesFrom(list)
        headPieces.forEach { piece ->
            diceBox.getAllNumbers().forEach { number ->
                val pieceAfterMove = piece.pieceAfterMove(number)
                if (pieceAfterMove != null && board.canMovePiece(piece, pieceAfterMove)) {
                    // TODO: 10/11/19 Kayvan: View Interaction for active piece
                    diceBox.updateDiceStateWith(number)
                }
            }
        }
    }
}