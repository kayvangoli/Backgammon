package com.k1apps.backgammon.buisness

import androidx.collection.ArrayMap

class Board {
    private val cells: ArrayMap<Int, ArrayList<Piece>> = ArrayMap()
    var pieceList1 = arrayListOf<Piece>()
    private set
    var pieceList2 = arrayListOf<Piece>()
    private set

    fun initBoard() {
        cells.clear()
        pieceList1.clear()
        pieceList2.clear()
        initLists()
    }

    private fun initLists() {
        for (item in 0 until 2) {
            createPieceAndSetToGame(1)
        }
        for (item in 0 until 5) {
            createPieceAndSetToGame(12)
        }
        for (item in 0 until 3) {
            createPieceAndSetToGame(17)
        }
        for (item in 0 until 5) {
            createPieceAndSetToGame(19)
        }
    }

    private fun createPieceAndSetToGame(location: Int) {
        val piece1 = Piece(MoveType.Normal, location)
        val piece2 = Piece(MoveType.Revers, location)

        pieceList1.add(piece1)
        pieceList2.add(piece2)

        setPieceToCell(piece1)
        setPieceToCell(piece2)
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