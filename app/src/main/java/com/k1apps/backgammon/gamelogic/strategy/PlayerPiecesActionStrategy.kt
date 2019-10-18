package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.gamelogic.Board
import com.k1apps.backgammon.gamelogic.DiceBox
import com.k1apps.backgammon.gamelogic.Piece

interface PlayerPiecesActionStrategy {
    fun updateDicesState(diceBox: DiceBox, list: ArrayList<Piece>, board: Board)
}