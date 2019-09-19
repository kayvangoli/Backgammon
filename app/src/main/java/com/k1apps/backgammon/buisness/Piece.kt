package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Utils.reverseLocation

class PieceImpl(private val moveType: MoveType) : Piece {

    override var state: PieceState = PieceState.IN_GAME

    override var location: Int = -1
    set(value) {
        if (value in 1..24) {
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
                if (canMoveWithNumber(number)) {
                    val location: Int
                    if (moveType == MoveType.Revers) {
                        location = this.location + number
                    } else {
                        location = this.location - number
                    }
                    piece.location = location
                } else {
                    return null
                }
            }
            PieceState.WON -> {
                return null
            }
        }
        return piece
    }

    private fun canMoveWithNumber(number: Byte): Boolean {
        if (moveType == MoveType.Normal) {
            return location - number > 0
        } else {
            return location + number < 25
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
