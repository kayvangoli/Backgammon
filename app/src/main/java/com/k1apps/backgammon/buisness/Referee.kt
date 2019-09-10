package com.k1apps.backgammon.buisness

class RefereeImpl(
    private val board: Board,
    private val turnaround: Turnaround,
    private val diceBox: DiceBox,
    private val player1: Player,
    private val player2: Player
) : Referee {

    override fun onThrewDice(player: Player, number: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onThrewDiceBox(player: Player, pair: Pair<Byte, Byte>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init {
        board.initBoard()
        player1.pieceList = board.pieceList1
        player2.pieceList = board.pieceList2
        player1.dice = diceBox.dice1
        player2.dice = diceBox.dice2
    }
}

interface Referee : RollListener {

}

