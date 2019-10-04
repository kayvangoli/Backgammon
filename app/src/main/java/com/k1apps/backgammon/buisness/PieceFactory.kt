package com.k1apps.backgammon.buisness

object PieceFactory {
    fun createReversePiece(): Piece {
        return PieceImpl(MoveType.Revers)
    }
    fun createNormalPiece(): Piece {
        return PieceImpl(MoveType.Normal)
    }
}