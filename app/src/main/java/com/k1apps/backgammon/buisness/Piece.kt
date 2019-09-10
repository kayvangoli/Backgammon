package com.k1apps.backgammon.buisness

import kotlin.math.abs

class PieceImpl(private val moveType: MoveType): Piece {
    override var location: Int = -1
        set(value) {
            if (moveType == MoveType.Revers) {
                field = reversLocation(value)
            } else {
                field = value
            }
        }

    private fun reversLocation(location: Int): Int {
        return abs(location - 25)
    }
}

interface Piece {
    var location: Int
}
