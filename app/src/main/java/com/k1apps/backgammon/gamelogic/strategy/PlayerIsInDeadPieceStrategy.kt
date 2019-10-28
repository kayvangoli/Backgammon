package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.gamelogic.*

class PlayerIsInDeadPieceStrategy :
    PlayerPiecesActionStrategy() {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val deadList = deadPieceList(list)
        if (deadList.isEmpty()) {
            throw ChooseStrategyException("There are no dead piece in list")
        }
        deadList.forEach { deadPiece ->
            diceBox.getAllNumbers().forEach { number->
                val pieceAfterMove = deadPiece.pieceAfterMove(number)
                pieceAfterMove?.let {
                    if (it.state == PieceState.IN_GAME && board.canMovePiece(deadPiece, it)) {
                        diceBox.updateDiceStateWith(number)
                    }
                }
            }
        }
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