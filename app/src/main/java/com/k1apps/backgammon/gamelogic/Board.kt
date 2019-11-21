package com.k1apps.backgammon.gamelogic

import androidx.collection.ArrayMap
import com.k1apps.backgammon.Constants.BOARD_LOCATION_RANGE
import com.k1apps.backgammon.Constants.DICE_RANGE
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.abs

interface Board {
    val pieceList1: ArrayList<Piece>
    val pieceList2: ArrayList<Piece>
    fun initBoard()
    fun canMovePiece(fromPiece: Piece?, toPiece: Piece?): Boolean
    fun isRangeFilledWithNormalPiece(range: IntRange): Boolean
    fun isRangeFilledWithReversePiece(range: IntRange): Boolean
    fun move(piece: Piece, number: Byte): Boolean
    fun getHeadPiece(cellNumber: Int): Piece?
    fun findDistanceBetweenTwoCell(startCell: Int, destinationCell: Int): Int
}

class BoardImpl @Inject constructor(
    override val pieceList1: ArrayList<Piece>, override val pieceList2: ArrayList<Piece>
) : Board {

    private val cells: ArrayMap<Int, ArrayList<Piece>> = ArrayMap()

    override fun initBoard() {
        clearCell()
        initLists()
        checkList()
    }

    private fun checkList() {
        var pieceCount = 0
        cells.forEach { cell ->
            if (cell.value.size > 0) {
                pieceCount += cell.value.size
                val moveType = cell.value[0].moveType
                cell.value.forEach {
                    if (it.moveType != moveType) {
                        throw CellFilledWithDifferencePieceException()
                    }
                }
            }
        }
        if (pieceCount != 30) {
            throw BoardPieceCountException("Piece count in board is: $pieceCount")
        }
    }

    private fun clearCell() {
        cells.forEach {
            it.value.clear()
        }
        cells.clear()
    }

    private fun initLists() {
        pieceList1.forEach {
            setPieceIntoCell(it)
        }
        pieceList2.forEach {
            setPieceIntoCell(it)
        }
    }

    override fun canMovePiece(fromPiece: Piece?, toPiece: Piece?): Boolean {
        if (fromPiece == null || toPiece == null) {
            return false
        }
        val cell = cells[toPiece.location]
        cell?.let {
            if (cell.size > 1 && cell[0].moveType != toPiece.moveType) {
                return false
            }
        }
        return true
    }

    override fun getHeadPiece(cellNumber: Int): Piece? {
        if (cellNumber !in BOARD_LOCATION_RANGE) {
            throw CellNumberException("Selected cell number is: $cellNumber")
        }
        val cell = cells[cellNumber]
        return cell?.get(0)
    }

    override fun findDistanceBetweenTwoCell(startCell: Int, destinationCell: Int): Int {
        if (startCell !in BOARD_LOCATION_RANGE) {
            throw CellNumberException("startCell number is: $startCell")
        }
        if (destinationCell !in BOARD_LOCATION_RANGE) {
            throw CellNumberException("destinationCell number is: $destinationCell")
        }
        return abs(startCell - destinationCell)
    }

    override fun isRangeFilledWithNormalPiece(range: IntRange): Boolean {
        range.forEach { index ->
            cells[index]?.let {
                if (it.size <= 1 || it[0].moveType == MoveType.Revers) {
                    return false
                }
            }
        }
        return true
    }

    override fun isRangeFilledWithReversePiece(range: IntRange): Boolean {
        range.forEach { index ->
            cells[index]?.let {
                if (it.size <= 1 || it[0].moveType == MoveType.Normal) {
                    return false
                }
            }
        }
        return true
    }

    override fun move(piece: Piece, number: Byte): Boolean {
        var moveCompleted = false
        if (number !in DICE_RANGE) {
            throw MoveException("Dice number range is incorrect")
        }
        if (piece.state == PieceState.WON) {
            throw MoveException("You can't move piece with won state")
        }
        val pieceAfterMove = piece.pieceAfterMove(number)
        if (pieceAfterMove == null) {
            removePieceFromCell(piece)
            piece.state = PieceState.WON
            moveCompleted = true
        } else {
            if (canMovePiece(piece, pieceAfterMove)) {
                piece.state = pieceAfterMove.state
                piece.location = pieceAfterMove.location
                moveCompleted = setPieceIntoCell(piece)
            }
        }
        return moveCompleted
    }

    private fun setPieceIntoCell(piece: Piece): Boolean {
        var isSet = false
        val arrayList = cells[piece.location]
        if (arrayList != null) {
            when {
                arrayList.size > 1 -> if (piece.moveType == arrayList[0].moveType) {
                    arrayList.add(piece)
                    isSet = true
                }
                arrayList.size == 1 -> isSet = if (piece.moveType == arrayList[0].moveType) {
                    arrayList.add(piece)
                    true
                } else {
                    val firstPiece = arrayList[0]
                    removePieceFromCell(firstPiece)
                    killPiece(firstPiece)
                    arrayList.add(piece)
                    true
                }
                else -> {
                    arrayList.add(piece)
                    isSet = true
                }
            }
        } else {
            val createdArray = arrayListOf<Piece>()
            createdArray.add(piece)
            cells[piece.location] = createdArray
            isSet = true
        }
        return isSet
    }

    private fun killPiece(piece: Piece) {
        piece.state = PieceState.DEAD
    }

    private fun removePieceFromCell(piece: Piece) {
        cells[piece.location]?.remove(piece)
    }
}