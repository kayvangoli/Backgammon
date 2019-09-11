package com.k1apps.backgammon.buisness

class TurnaroundImpl(private val diceBox: DiceBox) : Turnaround {
    private var player1: Player? = null
    private var player2: Player? = null
    private var player1DiceNumber: Byte = -1

    @Synchronized
    override fun onThrewDice(player: Player, number: Byte) {
        if (player1 == null) {
            player1 = player
            player1DiceNumber = number
        } else {
            player2 = player
            when {
                number < player1DiceNumber -> {
                    player1!!.diceBox = diceBox
                    retakeDices()
                }
                number > player1DiceNumber -> {
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

interface Turnaround : DiceRollCallback {

}