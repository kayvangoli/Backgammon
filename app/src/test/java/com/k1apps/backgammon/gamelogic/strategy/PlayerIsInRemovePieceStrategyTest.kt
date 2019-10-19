package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants.NORMAL_PIECE_LIST
import com.k1apps.backgammon.Constants.REVERSE_PIECE_LIST
import com.k1apps.backgammon.dagger.BoardModule
import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PieceListModule
import com.k1apps.backgammon.gamelogic.*
import dagger.Component
import dagger.Module
import dagger.Provides
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class PlayerIsInRemovePieceStrategyTest {

    @Inject
    lateinit var playerPiecesActionStrategy: PlayerPiecesActionStrategy
    @Inject
    @field:Named(NORMAL_PIECE_LIST)
    lateinit var normalPieceList: ArrayList<Piece>
    @Inject
    @field:Named(REVERSE_PIECE_LIST)
    lateinit var reversePieceList: ArrayList<Piece>
    @Inject
    lateinit var board: Board
    @Inject
    lateinit var diceBox: DiceBox
    @Inject
    lateinit var random: Random

    @Before
    fun setUp() {
        DaggerPlayerInRemovePieceComponentTest.builder()
            .setBoardModule(SpyBoardModule())
            .setDiceBoxModule(SpyDiceBoxModuleTest())
            .setPieceListModule(SpyPieceListModule())
            .build()
            .inject(this)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_5_and_piece_list_has_locations_4_5_then_diceBox_updateDiceStateWith_4_5_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        `when`(diceBox.getAllNumbers()).thenReturn(arrayListOf(4, 5))
        normalPieceList[0].location = 4
        normalPieceList[1].location = 5
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(5)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_updateDiceState_called_and_a_piece_is_dead_then_throw_exception() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        normalPieceList[0].state = PieceState.DEAD
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_updateDiceState_called_and_a_piece_is_not_in_home_range_then_throw_exception() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        normalPieceList[0].location = 14
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
    }
}

@GameScope
@Component(
    modules = [
        PlayerInRemovePieceModuleTest::class,
        PieceListModule::class,
        BoardModule::class,
        DiceBoxModule::class
    ]
)
interface PlayerInRemovePieceComponentTest {
    @Component.Builder
    interface Builder {
        fun setPieceListModule(pieceListModule: PieceListModule): Builder
        fun setBoardModule(boardModule: BoardModule): Builder
        fun setDiceBoxModule(diceBoxModule: DiceBoxModule): Builder
        fun build(): PlayerInRemovePieceComponentTest
    }
    fun inject(playerIsInRemovePieceStrategyTest: PlayerIsInRemovePieceStrategyTest)
}

@Module
class PlayerInRemovePieceModuleTest {
    @Provides
    fun providePlayerInPieceStrategy(): PlayerPiecesActionStrategy {
        return PlayerIsInRemovePieceStrategy()
    }
}