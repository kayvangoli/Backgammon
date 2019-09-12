package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

class PlayerImpl(override val playerType: PlayerType = PlayerType.LocalPlayer) : Player {
    override var dice: Dice? = null
    override var diceBox: DiceBox? = null
    override var pieceList: ArrayList<Piece>? = null

    override fun roll() {
        when {
            dice != null -> EventBus.getDefault().post(DiceThrownEvent(this, dice!!.roll()))
        }
    }

    override fun retakeDice() {
        dice = null
    }
}

interface Player {
    var pieceList: ArrayList<Piece>?
    var diceBox: DiceBox?
    var dice: Dice?
    val playerType: PlayerType
    fun roll()
    fun retakeDice()
}

enum class PlayerType {
    LocalPlayer
}
