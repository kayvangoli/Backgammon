package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.*
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import dagger.Component
import org.greenrobot.eventbus.EventBus
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
        val diceBoxMock: DiceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        player.diceBox = diceBoxMock
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        player.roll()
        verify(player.diceBox!!.dice1, times(1)).roll()
        verify(player.diceBox!!.dice2, times(1)).roll()
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_with_dice_box_then_check_dices_by_all_piece_list() {
        player.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((player.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((player.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        player.updateDicesStateInDiceBox()
        for (piece in player.pieceList) {
            verify(piece, times(1)).pieceAfterMove(6)
            verify(piece, times(1)).pieceAfterMove(5)
        }
    }

    @Test
    fun when_updateDicesStateInDiceBox_called_then_check_pieces_for_move() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(ArgumentMatchers.anyByte())).thenReturn(piece)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    diceBoxMock.dice1.number!!
                )
            ).thenReturn(true)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    diceBoxMock.dice2.number!!
                )
            ).thenReturn(true)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, times(15)).updateDiceStateWith(2)
        verify(diceBoxMock, times(15)).updateDiceStateWith(3)
        player.pieceList.forEach {
            verify(it, times(1)).pieceAfterMove(2)
            verify(it, times(1)).pieceAfterMove(3)
        }
    }

    @Test
    fun when_UpdateDicesStateInDiceBox_called_with_dices_2_and_3_and_player_can_use_2_but_can_not_use_3_then_disable_dice_3_and_enable_dice_2_check_with_board() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(ArgumentMatchers.anyByte())).thenReturn(piece)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    2
                )
            ).thenReturn(true)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    3
                )
            ).thenReturn(false)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, atLeastOnce()).updateDiceStateWith(2)
        verify(diceBoxMock, never()).updateDiceStateWith(3)
    }

    @Test
    fun when_UpdateDicesStateInDiceBox_called_with_dices_2_and_3_and_player_can_use_2_but_can_not_use_3_then_disable_dice_3_and_enable_dice_2_check_with_pieces() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(2)).thenReturn(piece)
            `when`(piece.pieceAfterMove(3)).thenReturn(null)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    2
                )
            ).thenReturn(true)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, atLeastOnce()).updateDiceStateWith(2)
        verify(diceBoxMock, never()).updateDiceStateWith(3)
    }

    @Test
    fun when_UpdateDicesStateInDiceBox_called_with_dices_2_and_3_and_player_can_use_both_of_the_dices_then_enable_dice_3_and_dice_2() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(ArgumentMatchers.anyByte())).thenReturn(piece)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    2
                )
            ).thenReturn(true)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    3
                )
            ).thenReturn(true)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, atLeastOnce()).updateDiceStateWith(2)
        verify(diceBoxMock, atLeastOnce()).updateDiceStateWith(3)
    }

    @Test
    fun when_UpdateDicesStateInDiceBox_called_with_dices_2_and_3_and_player_can_not_use_both_of_the_dices_then_disable_dice_3_and_dice_2_check_with_board() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(ArgumentMatchers.anyByte())).thenReturn(piece)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    2
                )
            ).thenReturn(false)
            `when`(
                board.canMovePiece(
                    NORMAL_HOME_RANGE,
                    piece,
                    3
                )
            ).thenReturn(false)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, never()).updateDiceStateWith(2)
        verify(diceBoxMock, never()).updateDiceStateWith(3)
    }

    @Test
    fun when_UpdateDicesStateInDiceBox_called_with_dices_2_and_3_and_player_can_not_use_both_of_the_dices_then_disable_dice_3_and_dice_2_check_with_pieces() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        player.diceBox = diceBoxMock
        player.pieceList.forEach { piece ->
            `when`(piece.pieceAfterMove(ArgumentMatchers.anyByte())).thenReturn(null)
        }
        player.updateDicesStateInDiceBox()
        verify(diceBoxMock, never()).updateDiceStateWith(2)
        verify(diceBoxMock, never()).updateDiceStateWith(3)
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
    override fun reverseList(): ArrayList<Piece> {
        val pieceList = arrayListOf<Piece>()
        super.reverseList().forEach {
            pieceList.add(spy(it))
        }
        return pieceList
    }

    override fun normalList(): ArrayList<Piece> {
        val pieceList = arrayListOf<Piece>()
        super.reverseList().forEach {
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