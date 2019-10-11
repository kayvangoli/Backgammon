package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Utils.reverseLocation

fun pieceListArrangementNormal(arrayList: ArrayList<Piece>) {
    setPiecesLocation(arrayList.subList(0, 2), 1)
    setPiecesLocation(arrayList.subList(2, 7), 12)
    setPiecesLocation(arrayList.subList(7, 10), 17)
    setPiecesLocation(arrayList.subList(10, 15), 19)
}

fun pieceListArrangementReverse(arrayList: ArrayList<Piece>) {
    setPiecesLocation(arrayList.subList(0, 2), reverseLocation(1))
    setPiecesLocation(arrayList.subList(2, 7), reverseLocation(12))
    setPiecesLocation(arrayList.subList(7, 10), reverseLocation(17))
    setPiecesLocation(arrayList.subList(10, 15), reverseLocation(19))
}

fun pieceListArrangement(arrayList: ArrayList<Piece>, configList: ArrangementListConfig) {
    var pieceIndex = 0
    configList.arrayConfig.forEach { arrangementConfig ->
        for (item in 1..arrangementConfig.count) {
            arrayList[pieceIndex].location = arrangementConfig.location
            pieceIndex++
        }
    }

}

fun pieceListArrangementInOneLocation(arrayList: ArrayList<Piece>, location: Int) {
    arrayList.forEach {
        it.location = location
    }
}

private fun setPiecesLocation(list: MutableList<Piece>, location: Int) {
    list.forEach {
        it.location = location
    }
}
