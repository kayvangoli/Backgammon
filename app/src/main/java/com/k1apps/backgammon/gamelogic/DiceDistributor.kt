package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DiceDistributorImpl(
    private val player1: Player,
    private val player2: Player,
    private val diceBox: DiceBox
) : DiceDistributor {

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
    override fun onEvent(event: DiceThrownEvent) {
        event.player.retakeDice()
        if (getOpponent(event.player).dice == null) {
            with(diceBox) {
                when {
                    dice1.number!! > dice2.number!! -> {
                        setDiceBox(player1)
                    }
                    dice1.number!! < dice2.number!! -> {
                        setDiceBox(player2)
                    }
                    else -> setDiceToPlayers()
                }
            }
        }
    }

    private fun setDiceBox(player: Player) {
        // TODO: 10/11/19 Kayvan: View Interaction
        if (player.haveDiedPiece()) {
            val opponent = getOpponent(player)
            if (opponent.isHomeRangeFill()) {
                opponent.diceBox = diceBox
            } else {
                player.diceBox = diceBox
            }
        } else {
            player.diceBox = diceBox
        }
    }

    @Subscribe
    override fun onEvent(event: DiceBoxThrownEvent) {
        with(event.player) {
            updateDicesStateInDiceBox()
            if (diceBox!!.isAtLeastOneDiceEnable().not()) {
                // TODO: 10/11/19 Kayvan: View interaction: no move
                retakeDiceBox()
                setDiceBox(getOpponent(this))
            }
        }
    }

    private fun getOpponent(player: Player): Player {
        if (player === player1) {
            return player2
        } else {
            return player1
        }
    }

    override fun start() {
        setDiceToPlayers()
    }

    private fun setDiceToPlayers() {
        player1.dice = diceBox.dice1
        player2.dice = diceBox.dice2
    }
}

interface DiceDistributor {
    fun start()
    fun whichPlayerHasDice(): Pair<Player, Player?>?
    fun onEvent(event: DiceThrownEvent)
    fun onEvent(event: DiceBoxThrownEvent)
}