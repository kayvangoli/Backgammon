package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.REVERSE_HOME_RANGE
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

class PlayerImpl(
    override val playerType: PlayerType = PlayerType.LocalPlayer,
    override val pieceList: ArrayList<Piece>,
    moveType: MoveType,
    private val board: Board
) : Player {
    override var dice: Dice? = null
    override var diceBox: DiceBox? = null
    override val homeCellIndexRange: IntRange = if (moveType == MoveType.Normal) {
        NORMAL_HOME_RANGE
    } else {
        REVERSE_HOME_RANGE
    }

    override fun roll() {
        dice?.let {
            it.roll()
            EventBus.getDefault().post(DiceThrownEvent(this))
        } ?: run {
            diceBox?.let {
                it.roll()
                EventBus.getDefault().post(DiceBoxThrownEvent(this))
            }
        }
    }

    override fun retakeDice() {
        dice = null
    }

    override fun retakeDiceBox() {
        diceBox = null
    }

    override fun updateDicesStateInDiceBox() {
        diceBox!!
        diceBox?.let { diceBox ->
            for (piece in pieceList) {
                val number1 = diceBox.dice1.number!!
                val number2 = diceBox.dice2.number!!
                piece.pieceAfterMove(number1)?.let {
                    if (board.canMovePiece(homeCellIndexRange, it, number1)) {
                        diceBox.updateDiceStateWith(number1)
                    }
                }
                piece.pieceAfterMove(number2)?.let {
                    if (board.canMovePiece(homeCellIndexRange, it, number2)) {
                        diceBox.updateDiceStateWith(number2)
                    }
                }
            }
        }
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
    fun retakeDiceBox()
    fun updateDicesStateInDiceBox()
}

enum class PlayerType {
    LocalPlayer, AndroidPlayer
}
