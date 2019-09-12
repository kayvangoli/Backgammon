package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

open class TurnaroundImpl(private val diceBox: DiceBox) : Turnaround {
    private var player1: Player? = null
    private var player2: Player? = null
    private var player1DiceNumber: Byte = -1

    init {
        EventBus.getDefault().register(this)
    }

    @Synchronized
    @Subscribe
    fun onEvent(event: DiceThrownEvent) {
        if (player1 == null) {
            player1 = event.player
            player1DiceNumber = event.number
        } else {
            player2 = event.player
            when {
                event.number < player1DiceNumber -> {
                    player1!!.diceBox = diceBox
                    retakeDices()
                }
                event.number > player1DiceNumber -> {
                    player2!!.diceBox = diceBox
                    retakeDices()
                }
            }
        }
    }

    private fun retakeDices() {
        player1!!.retakeDice()
        player2!!.retakeDice()
    }

}

interface Turnaround {
}