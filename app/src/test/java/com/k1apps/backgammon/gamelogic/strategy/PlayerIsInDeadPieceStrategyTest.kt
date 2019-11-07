package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.Constants
import com.k1apps.backgammon.dagger.BoardModule
import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PieceListModule
import com.k1apps.backgammon.gamelogic.*
import dagger.Component
import dagger.Module
import dagger.Provides
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class PlayerIsInDeadPieceStrategyTest {

    @Inject
    lateinit var playerPiecesActionStrategy: PlayerPiecesActionStrategy
    @Inject
    @field:Named(Constants.NORMAL_PIECE_LIST)
    lateinit var normalPieceList: ArrayList<Piece>
    @Inject
    @field:Named(Constants.REVERSE_PIECE_LIST)
    lateinit var reversePieceList: ArrayList<Piece>
    @Inject
    lateinit var board: Board
    @Inject
    lateinit var diceBox: DiceBox
    @Inject
    lateinit var random: Random

    @Before
    fun setUp() {
        DaggerPlayerInDeadPieceComponentTest.builder()
            .setBoardModule(SpyBoardModule())
            .setDiceBoxModule(SpyDiceBoxModuleTest())
            .setPieceListModule(SpyPieceListModule())
            .build()
            .inject(this)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_5_and_two_pieces_are_dead_and_opponent_cells_4_and_5_are_empty_then_diceBox_updateDiceStateWith_4_5_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        reversePieceList[0].state = PieceState.DEAD
        reversePieceList[1].state = PieceState.DEAD
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(5)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_5_and_one_piece_is_dead_and_opponent_cells_4_and_5_are_fill_then_diceBox_updateDiceStateWith_4_5_never_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val fromPiece = reversePieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(false)
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(5))).thenReturn(false)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(5)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_5_and_two_piece_is_dead_and_opponent_cell_4_is_empty_and_5_is_fill_then_diceBox_updateDiceStateWith_5_never_must_be_called_and_4_atLeast_once_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val fromPiece = reversePieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(true)
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(5))).thenReturn(false)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(5)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_one_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val fromPiece = reversePieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_two_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val piece1 = reversePieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = reversePieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_three_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_three_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val piece1 = reversePieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = reversePieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        val piece3 = reversePieceList[2]
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(piece3, piece3.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_four_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_four_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        val piece1 = reversePieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = reversePieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        val piece3 = reversePieceList[2]
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(piece3, piece3.pieceAfterMove(4))).thenReturn(true)
        val piece4 = reversePieceList[3]
        piece4.state = PieceState.DEAD
        `when`(board.canMovePiece(piece4, piece4.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_5_and_two_pieces_are_dead_and_opponent_cells_4_and_5_are_empty_then_diceBox_updateDiceStateWith_4_5_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        normalPieceList[0].state = PieceState.DEAD
        normalPieceList[1].state = PieceState.DEAD
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(5)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_5_and_one_piece_is_dead_and_opponent_cells_4_and_5_are_fill_then_diceBox_updateDiceStateWith_4_5_never_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val fromPiece = normalPieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(false)
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(5))).thenReturn(false)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(5)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_5_and_two_piece_is_dead_and_opponent_cell_4_is_empty_and_5_is_fill_then_diceBox_updateDiceStateWith_5_never_must_be_called_and_4_atLeast_once_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val fromPiece = normalPieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(true)
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(5))).thenReturn(false)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(5)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_one_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val fromPiece = normalPieceList[0]
        fromPiece.state = PieceState.DEAD
        `when`(board.canMovePiece(fromPiece, fromPiece.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_two_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val piece1 = normalPieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = normalPieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_three_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_three_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val piece1 = normalPieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = normalPieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        val piece3 = normalPieceList[2]
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(piece3, piece3.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_four_piece_is_dead_and_opponent_cell_4_is_empty_then_diceBox_updateDiceStateWith_4_four_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        val piece1 = normalPieceList[0]
        piece1.state = PieceState.DEAD
        `when`(board.canMovePiece(piece1, piece1.pieceAfterMove(4))).thenReturn(true)
        val piece2 = normalPieceList[1]
        piece2.state = PieceState.DEAD
        `when`(board.canMovePiece(piece2, piece2.pieceAfterMove(4))).thenReturn(true)
        val piece3 = normalPieceList[2]
        piece3.state = PieceState.DEAD
        `when`(board.canMovePiece(piece3, piece3.pieceAfterMove(4))).thenReturn(true)
        val piece4 = normalPieceList[3]
        piece4.state = PieceState.DEAD
        `when`(board.canMovePiece(piece4, piece4.pieceAfterMove(4))).thenReturn(true)
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_updateDiceState_called_and_no_any_piece_dead_then_throw_exception() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_move_called_and_piece_is_alive_then_thrown_chooseStrategyException() {
        playerPiecesActionStrategy.move(diceBox.dice1, normalPieceList[0], board)
    }

    @Test
    fun when_move_called_then_move_should_be_true_and_board_move_called() {
        val piece = normalPieceList[0]
        piece.state = PieceState.DEAD
        `when`(diceBox.dice1.number).thenReturn(4)
        val move = playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
        verify(board).move(piece, diceBox.dice1.number!!)
        assertTrue(move)
    }

    @Test
    fun when_move_called_with_reverse_piece_then_move_should_be_true_and_board_move_called() {
        val piece = reversePieceList[0]
        piece.state = PieceState.DEAD
        `when`(diceBox.dice1.number).thenReturn(4)
        val move = playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
        verify(board).move(piece, diceBox.dice1.number!!)
        assertTrue(move)
    }

    @Test(expected = CellNumberException::class)
    fun when_findDice_called_and_both_of_fromCell_and_toCell_are_null_then_throw_CellNumberException() {
        playerPiecesActionStrategy.findDice(null, null, diceBox, board)
    }

    @Test(expected = CellNumberException::class)
    fun when_findDice_called_and_fromCell_is_null_and_to_cell_is_not_in_dice_range_then_throw_CellNumberException() {
        playerPiecesActionStrategy.findDice(null, 8, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_findDice_called_and_fromCell_is_not_null_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(6, 8, diceBox, board)
    }

    @Test
    fun when_findDice_called_and_fromCell_is_null_and_to_cell_is_5_then_diceBox_getDiceWithNumber_should_be_called() {
        val mockDiceBox = mock(DiceBox::class.java)
        playerPiecesActionStrategy.findDice(null, 5, mockDiceBox, board)
        verify(mockDiceBox).getActiveDiceWithNumber(5)
    }
}

@GameScope
@Component(
    modules = [
        PlayerInDeadPieceModuleTest::class,
        PieceListModule::class,
        BoardModule::class,
        DiceBoxModule::class
    ]
)
interface PlayerInDeadPieceComponentTest {
    @Component.Builder
    interface Builder {
        fun setPieceListModule(pieceListModule: PieceListModule): Builder
        fun setBoardModule(boardModule: BoardModule): Builder
        fun setDiceBoxModule(diceBoxModule: DiceBoxModule): Builder
        fun build(): PlayerInDeadPieceComponentTest
    }
    fun inject(playerIsInDeadPieceStrategyTest: PlayerIsInDeadPieceStrategyTest)
}

@Module
class PlayerInDeadPieceModuleTest {
    @Provides
    fun providePlayerInPieceStrategy(): PlayerPiecesActionStrategy {
        return PlayerIsInDeadPieceStrategy()
    }
}