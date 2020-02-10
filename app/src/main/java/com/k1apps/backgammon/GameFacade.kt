package com.k1apps.backgammon

import com.k1apps.backgammon.gamelogic.PlayerType
import com.k1apps.backgammon.gamelogic.Referee

interface GameFacade {
    fun roll(playerType: PlayerType)
}

class GameFacadeImpl(private val referee: Referee) : GameFacade {
    override fun roll(playerType: PlayerType) {
//        val player = referee.getPlayerHasDiceBox(playerType)
//        player?.let {
//
//        }
    }
}