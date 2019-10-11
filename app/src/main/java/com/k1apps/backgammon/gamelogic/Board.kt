package com.k1apps.backgammon.gamelogic

import androidx.collection.ArrayMap
import javax.inject.Inject

interface Board {
    val pieceList1: ArrayList<Piece>
    val pieceList2: ArrayList<Piece>
    fun initBoard()
    fun canMovePiece(homeCellIndexRange: IntRange, piece: Piece, number: Byte): Boolean
    fun isRangeFilledWithNormalPiece(range: IntRange): Boolean
    fun isRangeFilledWithReversePiece(range: IntRange): Boolean
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
            setPieceToCell(it)
        }
        pieceList2.forEach {
            setPieceToCell(it)
        }
    }

    override fun canMovePiece(homeCellIndexRange: IntRange, piece: Piece, number: Byte): Boolean {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //check died piece
        //check remove
        //check move
        return false
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

    private fun setPieceToCell(piece: Piece) {
        val array = cells[piece.location]
        if (array != null) {
            array.add(piece)
        } else {
            val arrayList = arrayListOf<Piece>()
            arrayList.add(piece)
            cells[piece.location] = arrayList
        }
    }
}