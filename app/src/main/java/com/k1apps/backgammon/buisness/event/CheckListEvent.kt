package com.k1apps.backgammon.buisness.event

import com.k1apps.backgammon.buisness.Piece

data class CheckListEvent(val homeRange: IntRange, val list: ArrayList<Piece>)