package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.*
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import dagger.Component
import org.greenrobot.eventbus.EventBus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import javax.inject.Inject
import org.mockito.ArgumentMatchers
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

    @Before
    fun setup() {
        DaggerPlayerComponentTest.builder().setPieceListModule(SpyPieceListModule())
            .setBoardModule(SpyBoardModule()).build().inject(this)
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
        val diceBoxMock: DiceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        player.diceBox = diceBoxMock
        player.roll()
        verify(diceDistributor, times(1)).onEvent(DiceBoxThrownEvent(player))
    }

    @Test
    fun when_retakeDice_called_then_dice_should_be_null() {
        val diceMock: Dice = mock(Dice::class.java)
        player.dice = diceMock
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }

    @Test
    fun when_retakeDiceBox_called_then_diceBox_should_be_null() {
        player.diceBox = mock(DiceBox::class.java)
        assertTrue(player.diceBox != null)
        player.retakeDiceBox()
        assertTrue(player.diceBox == null)
    }

    @Test
    fun when_roll_called_with_dice_box_then_roll_dice_box() {
        val diceBoxMock: DiceBox = mock(DiceBox::class.java)
        player.diceBox = diceBoxMock
        player.roll()
        verify(player.diceBox, times(1))!!.roll()
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_5_and_have_dead_piece_and_both_two_cells_are_filled_by_opponent_then_dices_must_be_disable() {
        player.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        player.pieceList[0].state = PieceState.DEAD
        `when`(board.canMovePiece(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox!!.dice1, never()).enabled = true
        verify(player.diceBox!!.dice2, never()).enabled = true
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_5_and_have_dead_piece_and_cell6_is_filled_by_opponent_then_dice6_must_be_disable_and_dice_5_enable() {
        player.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        val piece = player.pieceList[0]
        piece.state = PieceState.DEAD
        `when`(board.canMovePiece(piece, piece.pieceAfterMove(6))).thenReturn(false)
        `when`(board.canMovePiece(piece, piece.pieceAfterMove(5))).thenReturn(true)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox!!.dice1, never()).enabled = true
        verify(player.diceBox!!.dice2, times(1)).enabled = true
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_5_and_have_dead_piece_and_both_of_two_cells_are_empty_then_two_dices_must_be_enable() {
        player.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        val piece = player.pieceList[0]
        piece.state = PieceState.DEAD
        `when`(board.canMovePiece(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox!!.dice1, times(1)).enabled = true
        verify(player.diceBox!!.dice2, times(1)).enabled = true
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_5_and_have_2_dead_piece_and_just_cell6_is_empty_then_dice1_must_be_enable_and_dice2_disable() {
        player.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        val piece = player.pieceList[0]
        val piece1 = player.pieceList[1]
        piece.state = PieceState.DEAD
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece, piece.pieceAfterMove(6))).thenReturn(true)
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(6))).thenReturn(true)
        `when`(board.canMovePiece(piece, piece.pieceAfterMove(5))).thenReturn(false)
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(5))).thenReturn(false)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox!!.dice1, atLeastOnce()).enabled = true
        verify(player.diceBox!!.dice2, never()).enabled = true
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_6_and_have_4_dead_piece_and_cell6_is_full_then_never_call_updateDicesStateWith6() {
        player.diceBox = spy(DiceBoxImpl(spy(DiceImpl()), spy(DiceImpl())))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(6)
        val piece = player.pieceList[0]
        val piece1 = player.pieceList[1]
        val piece2 = player.pieceList[1]
        val piece3 = player.pieceList[1]
        piece.state = PieceState.DEAD
        piece1.state = PieceState.DEAD
        piece2.state = PieceState.DEAD
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(false)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox, never())!!.updateDiceStateWith(6)
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dices_6_and_6_and_have_4_dead_piece_and_cell6_is_empty_then_at_least_4_time_call_updateDicesStateWith6() {
        player.diceBox = spy(DiceBoxImpl(spy(DiceImpl()), spy(DiceImpl())))
        `when`(player.diceBox!!.dice1.number).thenReturn(6)
        `when`(player.diceBox!!.dice2.number).thenReturn(6)
        val piece = player.pieceList[0]
        val piece1 = player.pieceList[1]
        val piece2 = player.pieceList[1]
        val piece3 = player.pieceList[1]
        piece.state = PieceState.DEAD
        piece1.state = PieceState.DEAD
        piece2.state = PieceState.DEAD
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true)
        player.updateDicesStateInDiceBox()
        verify(player.diceBox, atLeast(4))!!.updateDiceStateWith(6)
    }

    @Test
    fun when_updateDiceStateInDiceBox_called_and_player_can_remove_piece_then_all_dices_must_be_enable(){
        player.diceBox = spy(DiceBoxImpl(spy(DiceImpl()), spy(DiceImpl())))
        player.pieceList.forEach {
            it.location = 1
        }
        player.updateDicesStateInDiceBox()
        verify(player.diceBox!!, atLeastOnce()).enable()
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
@Component(modules = [PlayerModule::class, PieceListModule::class, BoardModule::class])
interface PlayerComponentTest {

    @Component.Builder
    interface Builder {
        fun setPieceListModule(pieceListModule: PieceListModule): Builder
        fun setBoardModule(boardModule: BoardModule): Builder
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