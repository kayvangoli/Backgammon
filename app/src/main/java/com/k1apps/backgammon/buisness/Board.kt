package com.k1apps.backgammon.buisness

import androidx.collection.ArrayMap
import javax.inject.Inject

interface Board {
    val pieceList1: ArrayList<Piece>
    val pieceList2: ArrayList<Piece>
    fun initBoard()
}

class BoardImpl @Inject constructor(
    override val pieceList1: ArrayList<Piece>, override val pieceList2: ArrayList<Piece>
) : Board {

    private val cells: ArrayMap<Int, ArrayList<Piece>> = ArrayMap()

    override fun initBoard() {
        initLists()
    }

    private fun initLists() {
        setPiecesToCells(pieceList1.subList(0, 2), 1)
        setPiecesToCells(pieceList1.subList(2, 7), 12)
        setPiecesToCells(pieceList1.subList(7, 10), 17)
        setPiecesToCells(pieceList1.subList(10, 15), 19)

        setPiecesToCells(pieceList2.subList(0, 2), 1)
        setPiecesToCells(pieceList2.subList(2, 7), 12)
        setPiecesToCells(pieceList2.subList(7, 10), 17)
        setPiecesToCells(pieceList2.subList(10, 15), 19)
    }

    private fun setPiecesToCells(pieces: MutableList<Piece>, location: Int) {
        for (piece in pieces) {
            piece.location = location
            setPieceToCell(piece)
        }
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