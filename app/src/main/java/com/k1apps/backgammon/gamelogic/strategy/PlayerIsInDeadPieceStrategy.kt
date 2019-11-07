package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants
import com.k1apps.backgammon.gamelogic.*

class PlayerIsInDeadPieceStrategy :
    PlayerPiecesActionStrategy() {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val deadList = deadPieceList(list)
        if (deadList.isEmpty()) {
            throw ChooseStrategyException("There are no dead piece in list")
        }
        deadList.forEach { deadPiece ->
            diceBox.getAllUnUsedNumbers().forEach { number->
                val pieceAfterMove = deadPiece.pieceAfterMove(number)
                pieceAfterMove?.let {
                    if (it.state == PieceState.IN_GAME && board.canMovePiece(deadPiece, it)) {
                        // TODO: 10/11/19 Kayvan: View Interaction for active piece
                        diceBox.updateDiceStateWith(number)
                    }
                }
            }
        }
    }

    override fun move(dice: Dice, piece: Piece, board: Board): Boolean {
        if (piece.state != PieceState.DEAD) {
            throw ChooseStrategyException("The selected piece is alive")
        }
        return board.move(piece, dice.number!!)
    }

    override fun findDice(
        fromCellNumber: Int?,
        toCellNumber: Int?,
        diceBox: DiceBox,
        board: Board
    ): Dice? {
        if (fromCellNumber == null && toCellNumber == null) {
            throw CellNumberException("Move method called where fromCellNumber and toCellNumber are null")
        }
        if (fromCellNumber != null) {
            throw ChooseStrategyException("There are no dead piece in list")
        }
        if (toCellNumber == null) {
            throw ChooseStrategyException("There are no dead piece in list")
        }
        if (toCellNumber !in Constants.DICE_RANGE) {
            throw CellNumberException("Move piece to Game with range greater than dice")
        }
        return diceBox.getActiveDiceWithNumber(toCellNumber)
    }

    private fun deadPieceList(list: ArrayList<Piece>): List<Piece> {
        val deadList = arrayListOf<Piece>()
        list.forEach {
            if (it.state == PieceState.DEAD) {
                deadList.add(it)
            }
        }
        return deadList
    }
}