package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.REVERSE_HOME_RANGE
import com.k1apps.backgammon.gamelogic.*

class PlayerIsInRemovePieceStrategy :
    PlayerPiecesActionStrategy {
    override fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board) {
        val homeCellIndexRange: IntRange = if (list[0].moveType == MoveType.Normal) {
            NORMAL_HOME_RANGE
        } else {
            REVERSE_HOME_RANGE
        }
        if (isInRemovePieceState(homeCellIndexRange, list).not()) {
            throw ChooseStrategyException("State is not remove piece")
        }
        list.forEach { piece ->
            diceBox.getAllNumbers().forEach { diceNumber ->
                if (piece.locationInMySide() == diceNumber.toInt()) {
                    diceBox.updateDiceStateWith(diceNumber)
                }
            }
        }
        if (diceBox.isEnable()) {
            return
        }
        diceBox.getAllNumbers().forEach { number->
            var canRemove = true
            list.forEach pieceLoop@{ piece->
                if (piece.locationInMySide() > number) {
                    canRemove = false
                    return@pieceLoop
                }
            }
            if (canRemove) {
                diceBox.updateDiceStateWith(number)
            }
        }
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