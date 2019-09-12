package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DiceDistributorImpl(
    private val player1: Player,
    private val player2: Player,
    private val diceBox: DiceBox
) : DiceDistributor {

    private var player1DiceNumber: Byte = -1

    init {
        EventBus.getDefault().register(this)
    }

    override fun whichPlayerHasDice(): Player? {
        if (player1.diceBox != null) {
            return player1
        }
        if (player2.diceBox != null) {
            return player2
        }
        return null
    }

    @Synchronized
    @Subscribe
    fun onEvent(event: DiceThrownEvent) {
        if (player1 == event.player) {
            player1DiceNumber = event.number
        } else {
            when {
                event.number < player1DiceNumber -> {
                    player1.diceBox = diceBox
                    retakeDices()
                }
                event.number > player1DiceNumber -> {
                    player2.diceBox = diceBox
                    retakeDices()
                }
            }
        }
    }

    private fun retakeDices() {
        player1.retakeDice()
        player2.retakeDice()
    }

    override fun start() {
        player1DiceNumber = -1
    }
}

interface DiceDistributor {
    fun start()
    fun whichPlayerHasDice(): Player?
}