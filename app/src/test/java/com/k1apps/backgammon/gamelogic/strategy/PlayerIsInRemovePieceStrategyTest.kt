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
import org.junit.Assert.assertTrue
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
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_5_and_piece_list_has_locations_4_5_then_diceBox_updateDiceStateWith_4_5_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        reversePieceList[0].location = 4
        reversePieceList[1].location = 5
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(5)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_there_are_4_piece_with_location_4_then_diceBox_updateDiceStateWith_4_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 4
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_all_pieces_locations_is_1_then_diceBox_updateDiceStateWith_atLeast_4_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_4_4_and_all_pieces_locations_are_1_except_one_which_is_5_then_diceBox_updateDiceStateWith_atleast4_times_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        reversePieceList[0].location = 5
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_cell_1_fiiled_by_opponent_then_diceBox_updateDiceStateWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_2_and_all_pieces_locations_is_4_and_cells_3_and_1_are_filled_by_the_opponent_then_diceBox_updateDiceStateWith_2_atLeast_1_time_must_be_called_but_1_never_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 4
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
            `when`(board.canMovePiece(it, it.pieceAfterMove(2))).thenReturn(true)
            `when`(board.canMovePiece(it, it.pieceAfterMove(3))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 2))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(2)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_cell_1_is_filled_by_the_opponent_then_diceBox_updateDiceStateWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_except_one_with_location_3_and_cell_1_is_filled_by_the_opponent_then_diceBox_updateDiceStateWith_4_and_1_atLeast_1_time_must_be_called() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        reversePieceList[0].location = 3
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, atLeast(1)).updateDiceStateWith(1)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_reverse_updateDiceState_called_and_a_piece_is_dead_then_throw_exception() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        reversePieceList[0].state = PieceState.DEAD
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_reverse_updateDiceState_called_and_a_piece_is_not_in_home_range_then_throw_exception() {
        normalPieceList.forEach {
            it.location = 24
        }
        reversePieceList.forEach {
            it.location = 1
        }
        reversePieceList[0].location = 14
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_5_and_piece_list_has_locations_4_5_then_diceBox_updateDiceStateWith_4_5_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 5))
        normalPieceList[0].location = 4
        normalPieceList[1].location = 5
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(5)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_there_are_4_piece_with_location_4_then_diceBox_updateDiceStateWith_4_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 4
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_all_pieces_locations_is_1_then_diceBox_updateDiceStateWith_atLeast_4_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_4_4_and_all_pieces_locations_are_1_except_one_which_is_5_then_diceBox_updateDiceStateWith_atleast4_times_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 1
        }
        normalPieceList[0].location = 5
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(4)).updateDiceStateWith(4)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_cell_1_filled_by_opponent_then_diceBox_updateDiceStateWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_2_and_all_pieces_locations_is_4_and_cells_3_and_1_are_filled_by_the_opponent_then_diceBox_updateDiceStateWith_2_atLeast_1_time_must_be_called_but_1_never_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 4
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
            `when`(board.canMovePiece(it, it.pieceAfterMove(2))).thenReturn(true)
            `when`(board.canMovePiece(it, it.pieceAfterMove(3))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 2))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(2)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_cell_1_is_filled_by_the_opponent_then_diceBox_updateDiceStateWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_and_all_pieces_locations_is_2_except_one_with_location_3_and_cell_1_is_filled_by_the_opponent_then_diceBox_updateDiceStateWith_4_and_1_atLeast_1_time_must_be_called() {
        reversePieceList.forEach {
            it.location = 24
        }
        normalPieceList.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        normalPieceList[0].location = 3
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeast(1)).updateDiceStateWith(4)
        verify(diceBox, atLeast(1)).updateDiceStateWith(1)
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

    @Test(expected = ChooseStrategyException::class)
    fun when_move_called_and_piece_location_is_out_of_home_range_then_thrown_chooseStrategyException() {
        val piece = normalPieceList[0]
        piece.location = 13
        playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_move_called_and_piece_state_is_not_inGame_then_thrown_chooseStrategyException() {
        val piece = normalPieceList[0]
        piece.state = PieceState.WON
        playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
    }

    @Test
    fun when_move_called_then_move_should_be_true_and_board_move_called() {
        val piece = normalPieceList[14]
        `when`(diceBox.dice1.number).thenReturn(4)
        val move = playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
        verify(board).move(piece, diceBox.dice1.number!!)
        assertTrue(move)
    }

    @Test
    fun when_move_called_with_reverse_piece_then_move_should_be_true_and_board_move_called() {
        val piece = reversePieceList[14]
        `when`(diceBox.dice1.number).thenReturn(4)
        val move = playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
        verify(board).move(piece, diceBox.dice1.number!!)
        assertTrue(move)
    }


    @Test(expected = CellNumberException::class)
    fun when_findDice_called_and_both_of_startCell_and_destinationCell_are_null_then_throw_CellNumberException() {
        playerPiecesActionStrategy.findDice(null, null, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_findDice_called_and_startCell_is_null_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(null, 8, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_findDice_called_and_startCell_is_not_in_DiceRange_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(13, 8, diceBox, board)
    }

    @Test
    fun when_findDice_called_and_startCell_is_6_and_to_cell_is_5_then_diceBox_getDiceWithNumber_1_and_board_findDistanceBetweenTwoCell_should_be_called() {
        val mockDiceBox = mock(DiceBox::class.java)
        playerPiecesActionStrategy.findDice(6, 5, mockDiceBox, board)
        verify(board).findDistanceBetweenTwoCell(6, 5)
        verify(mockDiceBox).getActiveDiceWithNumber(1)
    }

    @Test
    fun when_findDice_called_and_startCell_is_2_and_to_cell_is_null_then_diceBox_getDiceGreaterEqual_2_should_be_called() {
        val mockDiceBox = mock(DiceBox::class.java)
        playerPiecesActionStrategy.findDice(2, null, mockDiceBox, board)
        verify(mockDiceBox).getActiveDiceGreaterEqual(2)
    }


    @Test
    fun given_findDice_called_when_startCell_is_6_and_to_cell_is_6_then_return_null() {
        val mockDiceBox = mock(DiceBox::class.java)
        val result = playerPiecesActionStrategy.findDice(6, 6, mockDiceBox, board)
        assertTrue(result == null)
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