package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.BOARD_LOCATION_RANGE
import com.k1apps.backgammon.Utils.reverseLocation

class PieceImpl(private val moveType: MoveType) : Piece {

    override var state: PieceState = PieceState.IN_GAME

    override var location: Int = -1
        set(value) {
            if (value in BOARD_LOCATION_RANGE) {
                field = value
            }
        }

    override fun pieceAfterMove(number: Byte): Piece? {
        assert(number in 1..7)
        val piece = PieceImpl(moveType)
        piece.state = state
        when (state) {
            PieceState.DEAD -> {
                var gotoEndNumber = number.toInt()
                if (moveType == MoveType.Normal) {
                    gotoEndNumber = reverseLocation(number.toInt())
                }
                piece.location = gotoEndNumber
            }
            PieceState.IN_GAME -> {
                if (moveType == MoveType.Revers) {
                    piece.location = this.location + number
                } else {
                    piece.location = this.location - number
                }
                return if (piece.location in BOARD_LOCATION_RANGE) {
                    piece
                } else {
                    null
                }
            }
            PieceState.WON -> {
                return null
            }
        }
        return piece
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

    override fun copy(): Piece {
        val cp = PieceImpl(moveType)
        cp.location = location
        cp.state = state
        return cp
    }
}

interface Piece {
    fun pieceAfterMove(number: Byte): Piece?
    var state: PieceState
    var location: Int
    fun copy(): Piece
}

enum class PieceState {
    DEAD, IN_GAME, WON
}
