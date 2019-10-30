package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.*
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import com.k1apps.backgammon.gamelogic.strategy.PlayerPiecesActionStrategy
import com.k1apps.backgammon.gamelogic.strategy.PlayerPiecesContextStrategy
import dagger.Component
import org.greenrobot.eventbus.EventBus
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import javax.inject.Inject
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import javax.inject.Named

@RunWith(MockitoJUnitRunner::class)
class PlayerTest {

    @Inject
    @field:Named(NORMAL_PLAYER)
    lateinit var player: Player

    @Mock
    lateinit var diceDistributor: DiceDistributorImpl
    @Inject
    lateinit var board: Board
    @Inject
    lateinit var diceBox: DiceBox
    @Inject
    lateinit var contextStrategy: PlayerPiecesContextStrategy

    @Before
    fun setup() {
        DaggerPlayerComponentTest.builder().setPieceListModule(SpyPieceListModule())
            .setBoardModule(SpyBoardModule())
            .setDiceBoxModule(SpyDiceBoxModuleTest())
            .setContextStrategyModule(SpyPlayerPiecesStrategyModule())
            .build().inject(this)
        EventBus.getDefault().register(diceDistributor)
    }

    @Test
    fun when_roll_called_with_dice_then_roll_dice_and_post_dice_thrown_event_callback_invoked() {
        val diceMock: Dice = mock(Dice::class.java)
        `when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        player.roll()
        verify(diceDistributor, times(1)).onEvent(DiceThrownEvent(player))
    }

    @Test
    fun when_roll_called_with_dice_then_roll_dice_and_post_dice_box_thrown_event_callback_invoked() {
        player.diceBox = diceBox
        player.roll()
        verify(diceDistributor, times(1)).onEvent(DiceBoxThrownEvent(player))
    }

    @Test
    fun when_retakeDice_called_then_dice_should_be_null() {
        player.dice = diceBox.dice1
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }

    @Test
    fun when_retakeDiceBox_called_then_diceBox_should_be_null() {
        player.diceBox = diceBox
        assertTrue(player.diceBox != null)
        player.retakeDiceBox()
        assertTrue(player.diceBox == null)
    }

    @Test
    fun when_roll_called_with_dice_box_then_roll_dice_box() {
        player.diceBox = diceBox
        player.roll()
        verify(player.diceBox, times(1))!!.roll()
    }

    @Test
    fun when_updateDiceStateInDiceBox_called_then_playerPieceContextStrategy_updateDicesState_should_be_called(){
        player.diceBox = diceBox
        player.updateDicesStateInDiceBox()
        verify(contextStrategy.getPlayerPiecesStrategy(player.pieceList))
            .updateDicesState(diceBox, player.pieceList, board)
    }

    @Test
    fun when_haveDiedPiece_called_and_piece_index_2_is_died_then_return_true() {
        player.pieceList[2].state = PieceState.DEAD
        assertTrue(player.haveDiedPiece())
    }

    @Test
    fun when_isHomeRangeFill_called_then_invoke_board_isHomeRangeFilled() {
        player.isHomeRangeFill()
        verify(board, times(1)).isRangeFilledWithNormalPiece(player.homeCellIndexRange)
    }
}


@GameScope
@Component(modules = [PlayerModule::class, PieceListModule::class,
    BoardModule::class, DiceBoxModule::class, PlayerPiecesStrategyModule::class])
interface PlayerComponentTest {

    @Component.Builder
    interface Builder {
        fun setPieceListModule(pieceListModule: PieceListModule): Builder
        fun setBoardModule(boardModule: BoardModule): Builder
        fun setDiceBoxModule(spyDiceBoxModuleTest: DiceBoxModule): Builder
        fun setContextStrategyModule(playerPiecesStrategyModule: PlayerPiecesStrategyModule): Builder
        fun build(): PlayerComponentTest
    }

    fun inject(playerTest: PlayerTest)
}

class SpyPieceListModule : PieceListModule() {
    override fun provideReverseList(): ArrayList<Piece> {
        val pieceList = arrayListOf<Piece>()
        super.provideReverseList().forEach {
            pieceList.add(spy(it))
        }
        return pieceList
    }

    override fun provideNormalList(): ArrayList<Piece> {
        val pieceList = arrayListOf<Piece>()
        super.provideReverseList().forEach {
            pieceList.add(spy(it))
        }
        return pieceList

    }
}

class SpyBoardModule : BoardModule() {
    override fun provideBoard(
        normalPieceList: ArrayList<Piece>,
        reversePieceList: ArrayList<Piece>
    ): Board {
        return spy(super.provideBoard(normalPieceList, reversePieceList))
    }
}

class SpyPlayerPiecesStrategyModule : PlayerPiecesStrategyModule() {
    override fun providePlayerPiecesContextStrategy(
        inGamePieceStrategy: PlayerPiecesActionStrategy,
        removePieceStrategy: PlayerPiecesActionStrategy,
        deadPieceStrategy: PlayerPiecesActionStrategy
    ): PlayerPiecesContextStrategy {
        return spy(super.providePlayerPiecesContextStrategy(
            inGamePieceStrategy,
            removePieceStrategy,
            deadPieceStrategy
        ))
    }

    override fun providePlayerIsInGamePieceStrategy(): PlayerPiecesActionStrategy {
        return spy(super.providePlayerIsInGamePieceStrategy())
    }

    override fun providePlayerIsInRemovePieceStrategy(): PlayerPiecesActionStrategy {
        return spy(super.providePlayerIsInRemovePieceStrategy())
    }

    override fun providePlayerIsInDeadPieceStrategy(): PlayerPiecesActionStrategy {
        return spy(super.providePlayerIsInDeadPieceStrategy())
    }
}