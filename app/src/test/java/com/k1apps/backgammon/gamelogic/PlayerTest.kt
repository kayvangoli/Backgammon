package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.*
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import com.k1apps.backgammon.gamelogic.event.MoveCompletedEvent
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
    fun given_roll_called_with_dice_then_roll_dice_and_post_dice_thrown_event_callback_invoked() {
        val diceMock: Dice = mock(Dice::class.java)
        `when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        player.roll()
        verify(diceDistributor, times(1)).onEvent(DiceThrownEvent(player))
    }

    @Test
    fun given_roll_called_with_dice_then_roll_dice_and_post_dice_box_thrown_event_callback_invoked() {
        player.diceBox = diceBox
        player.roll()
        verify(diceDistributor, times(1)).onEvent(DiceBoxThrownEvent(player))
    }

    @Test
    fun given_retakeDice_called_then_dice_should_be_null() {
        player.dice = diceBox.dice1
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }

    @Test
    fun given_retakeDiceBox_called_then_diceBox_should_be_null() {
        player.diceBox = diceBox
        assertTrue(player.diceBox != null)
        player.retakeDiceBox()
        assertTrue(player.diceBox == null)
    }

    @Test
    fun given_roll_called_with_dice_box_then_roll_dice_box() {
        player.diceBox = diceBox
        player.roll()
        verify(player.diceBox, times(1))!!.roll()
    }

    @Test
    fun given_updateDiceStateInDiceBox_called_then_playerPieceContextStrategy_updateDicesState_should_be_called() {
        player.diceBox = diceBox
        player.updateDicesStateInDiceBox()
        verify(contextStrategy.getPlayerPiecesStrategy(player.pieceList))
            .updateDicesState(diceBox, player.pieceList, board)
    }

    @Test
    fun given_haveDiedPiece_called_when_piece_index_2_is_died_then_return_true() {
        player.pieceList[2].state = PieceState.DEAD
        assertTrue(player.haveDiedPiece())
    }

    @Test
    fun given_isHomeRangeFill_called_then_invoke_board_isHomeRangeFilled() {
        player.isHomeRangeFill()
        verify(board, times(1)).isRangeFilledWithNormalPiece(player.homeCellIndexRange)
    }

    @Test(expected = MoveException::class)
    fun given_move_called_when_player_does_not_have_diceBox_then_throw_MoveException() {
        player.move(null, null)
    }

    @Test
    fun given_move_24_23_called_when_dice1_number_is_1_then_moveCompletedEvent_should_be_called() {
        board.initBoard()
        val fromCellNumber = 24
        val toCellNumber = 23
        val mockedDice = mock(Dice::class.java)
        `when`(mockedDice.number).thenReturn(1)
        val diceBox = mock(DiceBox::class.java)
        `when`(diceBox.getActiveDiceWithNumber(1)).thenReturn(mockedDice)
        player.diceBox = diceBox
        player.move(fromCellNumber, toCellNumber)
        verify(diceDistributor).onEvent(MoveCompletedEvent(player))
    }

    @Test
    fun given_move_24_23_called_when_dice_number_is_1_then_diceBox_useDice_should_be_called() {
        board.initBoard()
        val fromCellNumber = 24
        val toCellNumber = 23
        val mockedDice = mock(Dice::class.java)
        `when`(mockedDice.number).thenReturn(1)
        val diceBox = mock(DiceBox::class.java)
        `when`(diceBox.getActiveDiceWithNumber(1)).thenReturn(mockedDice)
        player.diceBox = diceBox
        player.move(fromCellNumber, toCellNumber)
        verify(diceBox).useDice(mockedDice)
    }

    @Test(expected = MoveException::class)
    fun given_move_with_null_and_2_called_when_player_has_not_dead_piece_then_throw_move_exception() {
        player.diceBox = diceBox
        player.move(null, 2)
    }
}


@GameScope
@Component(
    modules = [PlayerModule::class, PieceListModule::class,
        BoardModule::class, DiceBoxModule::class, PlayerPiecesStrategyModule::class]
)
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
        return spy(
            super.providePlayerPiecesContextStrategy(
                inGamePieceStrategy,
                removePieceStrategy,
                deadPieceStrategy
            )
        )
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