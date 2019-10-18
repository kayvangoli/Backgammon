package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.NORMAL_PIECE_LIST
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PieceListModule
import com.k1apps.backgammon.dagger.PlayerPiecesStrategyModule
import com.k1apps.backgammon.gamelogic.Piece
import com.k1apps.backgammon.gamelogic.PieceState
import dagger.Component
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

class PlayerPiecesContextStrategyImplTest {

    @Inject
    lateinit var playerPiecesContextStrategy: PlayerPiecesContextStrategy
    @Inject
    @field:Named(NORMAL_PIECE_LIST)
    lateinit var pieceList: ArrayList<Piece>

    @Before
    fun setUp() {
        DaggerPlayerPiecesContextStrategyComponent.create().inject(this)
    }

    @Test
    fun when_list_has_dead_and_getPlayerPiecesStrategy_called_then_return_deadStrategy() {
        pieceList[0].state = PieceState.DEAD
        val instance = playerPiecesContextStrategy.getPlayerPiecesStrategy(pieceList)
        assertTrue(instance is PlayerIsInDeadPieceStrategy)
    }

    @Test
    fun when_all_pieces_is_in_home_range_and_getPlayerPiecesStrategy_called_then_return_removeStrategy() {
        pieceList.forEach {
            it.location = 1
        }
        val instance = playerPiecesContextStrategy.getPlayerPiecesStrategy(pieceList)
        assertTrue(instance is PlayerIsInRemovePieceStrategy)
    }

    @Test
    fun when_all_pieces_is_in_home_range_but_one_piece_is_dead_and_getPlayerPiecesStrategy_called_then_return_deadStrategy() {
        pieceList.forEach {
            it.location = 1
        }
        pieceList[5].state = PieceState.DEAD
        val instance = playerPiecesContextStrategy.getPlayerPiecesStrategy(pieceList)
        assertTrue(instance is PlayerIsInDeadPieceStrategy)
    }

    @Test
    fun when_all_pieces_is_in_home_range_but_one_piece_is_not_and_getPlayerPiecesStrategy_called_then_return_InGameStrategy() {
        pieceList.forEach {
            it.location = 1
        }
        pieceList[8].location = 15
        val instance = playerPiecesContextStrategy.getPlayerPiecesStrategy(pieceList)
        assertTrue(instance is PlayerIsInGamePieceStrategy)
    }

    @Test
    fun when_all_pieces_is_in_home_range_but_one_piece_is_not_but_state_is_won_and_getPlayerPiecesStrategy_called_then_return_RemoveStrategy() {
        pieceList.forEach {
            it.location = 1
        }
        pieceList[8].location = 15
        pieceList[8].state = PieceState.WON
        val instance = playerPiecesContextStrategy.getPlayerPiecesStrategy(pieceList)
        assertTrue(instance is PlayerIsInRemovePieceStrategy)
    }
}

@GameScope
@Component(modules = [PlayerPiecesStrategyModule::class, PieceListModule::class])
interface PlayerPiecesContextStrategyComponent {
    fun inject(playerPiecesContextStrategyImplTest: PlayerPiecesContextStrategyImplTest)

}