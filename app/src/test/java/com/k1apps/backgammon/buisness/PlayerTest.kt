package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.NORMAL_HOME_RANGE
import com.k1apps.backgammon.Constants.NORMAL_PIECE_LIST
import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.BoardModule
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PieceListModule
import dagger.Component
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList
import org.mockito.ArgumentMatchers


class PlayerTest {
    @Inject
    lateinit var player: Player
    @Inject
    @field:Named("playerWithMockedList")
    lateinit var playerMockedList: Player
    @Inject
    lateinit var diceDistributorImpl: DiceDistributorImpl

    @Inject
    @field:Named("mockBoard")
    lateinit var board: Board

    @Before
    fun setup() {
        DaggerPlayerComponentTest.create().inject(this)
    }

    @Test
    fun when_roll_called_with_dice_then_roll_dice_and_post_dice_thrown_event_callback_invoked() {
        val diceMock: Dice = mock(Dice::class.java)
        Mockito.`when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        player.roll()
        verify(diceDistributorImpl, times(1)).onEvent(DiceThrownEvent(player, 2))
    }

    @Test
    fun when_retakeDice_called_then_dice_should_be_null() {
        val diceMock: Dice = mock(Dice::class.java)
        `when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }

    @Test
    fun when_roll_called_with_dice_box_then_roll_dice_box() {
        val diceBoxMock: DiceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        playerMockedList.diceBox = diceBoxMock
        `when`((playerMockedList.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((playerMockedList.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        playerMockedList.roll()
        verify(playerMockedList.diceBox!!.dice1, times(1)).roll()
        verify(playerMockedList.diceBox!!.dice2, times(1)).roll()
    }

    @Test
    fun when_roll_called_with_dice_box_then_check_dices_by_all_piece_list() {
        playerMockedList.diceBox = DiceBoxImpl(mock(Dice::class.java), mock(Dice::class.java))
        `when`((playerMockedList.diceBox as DiceBoxImpl).dice1.number).thenReturn(6)
        `when`((playerMockedList.diceBox as DiceBoxImpl).dice2.number).thenReturn(5)
        playerMockedList.roll()
        for (piece in playerMockedList.pieceList) {
            verify(piece, times(1)).pieceAfterMove(6)
            verify(piece, times(1)).pieceAfterMove(5)
        }
    }

    @Test
    fun when_roll_called_with_dice_box_then_check_pieces_for_move() {
        val diceBoxMock = mock(DiceBox::class.java)
        `when`(diceBoxMock.dice1).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice2).thenReturn(mock(Dice::class.java))
        `when`(diceBoxMock.dice1.number).thenReturn(2)
        `when`(diceBoxMock.dice2.number).thenReturn(3)
        playerMockedList.diceBox = diceBoxMock
        playerMockedList.pieceList.forEach { piece ->
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
        playerMockedList.roll()
        verify(diceBoxMock, times(15)).canUseDiceWith(2)
        verify(diceBoxMock, times(15)).canUseDiceWith(3)
        playerMockedList.pieceList.forEach {
            verify(it, times(1)).pieceAfterMove(2)
            verify(it, times(1)).pieceAfterMove(3)
        }
    }
}


@GameScope
@Component(modules = [PlayerModuleTest::class, PieceListModule::class, BoardModule::class])
interface PlayerComponentTest {
    fun inject(playerTest: PlayerTest)
}


@Module(includes = [PieceListModule::class, BoardModule::class])
class PlayerModuleTest {
    @GameScope
    @Provides
    fun providePlayer(
        @Named(NORMAL_PIECE_LIST) pieceList: ArrayList<Piece>,
        board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal, board)
    }

    @GameScope
    @Provides
    @Named("playerWithMockedList")
    fun providePlayerWithMockedList(
        @Named("mockedPieceList") pieceList: ArrayList<Piece>,
        @Named("mockBoard") board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal, board)
    }

    @GameScope
    @Provides
    @Named("mockedPieceList")
    fun provideMockedList(): ArrayList<Piece> {
        val mockPieceList = arrayListOf<Piece>()
        for (i in 0 until 15) {
            val mockPiece = mock(Piece::class.java)
            mockPieceList.add(mockPiece)
        }
        return mockPieceList
    }

    @GameScope
    @Provides
    @Named("mockBoard")
    fun provideBoardMock(): Board {
        return mock(Board::class.java)
    }

    @GameScope
    @Provides
    fun provideDiceDistributor(): DiceDistributorImpl {
        val mockTurnaroundImpl = mock(DiceDistributorImpl::class.java)
        EventBus.getDefault().register(mockTurnaroundImpl)
        return mockTurnaroundImpl
    }
}
