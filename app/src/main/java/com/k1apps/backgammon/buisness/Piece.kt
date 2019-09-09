package com.k1apps.backgammon.buisness

import kotlin.math.abs

class Piece(private val moveType: MoveType, location: Int) {
    var location: Int
        private set

    init {
        if (moveType == MoveType.Revers) {
            this.location = reversLocation(location)
        } else {
            this.location = location
        }
    }

    private fun reversLocation(location: Int): Int {
        return abs(location - 25)
    }
}

enum class MoveType {
    Normal, Revers
}
