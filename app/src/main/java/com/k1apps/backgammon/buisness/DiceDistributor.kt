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

    override fun whichPlayerHasDice(): Pair<Player, Player?>? {
        if (player1.diceBox != null) {
            return Pair(player1, null)
        }
        if (player2.diceBox != null) {
            return Pair(player2, null)
        }
        if (player1.dice != null && player2.dice != null) {
            return Pair(player1, player2)
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
        player1.dice = diceBox.dice1
        player2.dice = diceBox.dice2
    }
}

interface DiceDistributor {
    fun start()
    fun whichPlayerHasDice(): Pair<Player, Player?>?
}