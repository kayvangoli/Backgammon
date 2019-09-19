package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.Companion.END_NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.Companion.END_REVERSE_HOME_RANGE
import com.k1apps.backgammon.Constants.Companion.START_NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.Companion.START_REVERSE_HOME_RANGE
import com.k1apps.backgammon.buisness.event.CheckListEvent
import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

class PlayerImpl(
    override val playerType: PlayerType = PlayerType.LocalPlayer,
    override val pieceList: ArrayList<Piece>,
    moveType: MoveType
) : Player {
    override var dice: Dice? = null
    override var diceBox: DiceBox? = null
    override val homeCellIndexRange: Pair<Int, Int> = if (moveType == MoveType.Normal) {
        Pair(START_NORMAL_HOME_RANGE, END_NORMAL_HOME_RANGE)
    } else {
        Pair(START_REVERSE_HOME_RANGE, END_REVERSE_HOME_RANGE)
    }


    override fun roll() {
        when {
            dice != null -> EventBus.getDefault().post(DiceThrownEvent(this, dice!!.roll()))
            diceBox != null -> {
                diceBox!!.roll()
                val expectPieceList = arrayListOf<Piece>()
                for (piece in pieceList) {
                    piece.pieceAfterMove(diceBox!!.dice1.number!!)?.let { expectPieceList.add(it) }
                    piece.pieceAfterMove(diceBox!!.dice2.number!!)?.let { expectPieceList.add(it) }
                }
                EventBus.getDefault().post(CheckListEvent(homeCellIndexRange, expectPieceList))
            }
        }
    }

    override fun retakeDice() {
        dice = null
    }
}

interface Player {
    val homeCellIndexRange: Pair<Int, Int>
    val pieceList: ArrayList<Piece>
    var diceBox: DiceBox?
    var dice: Dice?
    val playerType: PlayerType
    fun roll()
    fun retakeDice()
}

enum class PlayerType {
    LocalPlayer, AndroidPlayer
}
