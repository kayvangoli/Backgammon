package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.REVERSE_HOME_RANGE
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
    override val homeCellIndexRange: IntRange = if (moveType == MoveType.Normal) {
        NORMAL_HOME_RANGE
    } else {
        REVERSE_HOME_RANGE
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
    val homeCellIndexRange: IntRange
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
