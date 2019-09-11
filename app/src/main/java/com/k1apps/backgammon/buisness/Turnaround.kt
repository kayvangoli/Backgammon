package com.k1apps.backgammon.buisness

class TurnaroundImpl : Turnaround {

    override fun onThrewDice(player: Player, number: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Turnaround : DiceRollCallback {
}