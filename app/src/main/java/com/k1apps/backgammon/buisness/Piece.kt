package com.k1apps.backgammon.buisness

import kotlin.math.abs

class PieceImpl(private val moveType: MoveType) : Piece {

    override var state: PieceState = PieceState.IN_GAME

    override var location: Int = -1
        set(value) {
            if (value > 24 || value < 1) {
                return
            }
            field = getCorrectLocation(value)
        }

    private fun reversLocation(location: Int): Int {
        return abs(location - 25)
    }

    override fun pieceAfterMove(number: Byte): Piece? {
        assert(number in 1..7)
        val piece = PieceImpl(moveType)
        piece.state = state
        when (state) {
            PieceState.DEAD -> {
                val gotoEndNumber = reversLocation(number.toInt())
                piece.location = gotoEndNumber
            }
            PieceState.IN_GAME -> {

            }
            PieceState.WON -> {
                return null
            }
        }
        return piece
    }

    private fun getCorrectLocation(value: Int): Int {
        return if (moveType == MoveType.Revers) {
            reversLocation(value)
        } else {
            value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is PieceImpl) {
            return other.hashCode() == hashCode()
        }
        return false
    }

    override fun hashCode(): Int {
        var result = moveType.hashCode()
        result = 31 * result + state.hashCode()
        result = 31 * result + location
        return result
    }
}

interface Piece {
    fun pieceAfterMove(number: Byte): Piece?
    var state: PieceState
    var location: Int
}

enum class PieceState {
    DEAD, IN_GAME, WON
}
