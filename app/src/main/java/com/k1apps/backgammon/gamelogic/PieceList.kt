package com.k1apps.backgammon.gamelogic

abstract class PieceList {
    val list : MutableList<Piece> = arrayListOf()
}

class NormalPieceList : PieceList() {
    init {
        for (item in 0 until 15) {
            list.add(PieceFactory.createNormalPiece())
            pieceListArrangementNormal(list)
        }
    }
}

class ReversePieceList : PieceList() {
    init {
        for (item in 0 until 15) {
            list.add(PieceFactory.createReversePiece())
            pieceListArrangementReverse(list)
        }
    }
}