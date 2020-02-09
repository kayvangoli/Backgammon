package com.k1apps.backgammon.gamelogic.strategy

import com.k1apps.backgammon.gamelogic.*
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlayerIsInRemovePieceStrategyTest {

    private lateinit var playerPiecesActionStrategy: PlayerPiecesActionStrategy
    @Mock
    private lateinit var board: Board
    @Mock
    private lateinit var diceBox: DiceBox
    @Mock
    private lateinit var lst: ArrayList<Piece>
    private var num1: Byte = 4
    private var num2: Byte = 5

    @Before
    fun setUp() {
        lst = arrayListOf()
        for (item in 0 until 15) {
            lst.add(spy(PieceFactory.createNormalPiece()))
        }
        lst.forEach {
            it.location = 1
        }
        playerPiecesActionStrategy = PlayerIsInRemovePieceStrategy()
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_4_5_and_piece_list_has_locations_4_5_then_diceBox_enableDiceWith_4_5_must_be_called() {
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(num1, num2))
        lst[0].location = num1.toInt()
        lst[1].location = num2.toInt()
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeastOnce()).enableDiceWith(num1)
        verify(diceBox, atLeastOnce()).enableDiceWith(num2)
    }

    @Test(expected = ChooseStrategyException::class)
    fun given_updateDiceBoxStatus_called_when_one_piece_is_won_and_ane_piece_in_location_12_then_throw_chooseStrategyException() {
        lst[0].state = PieceState.WON
        lst[1].location = 12
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_4_4_and_there_are_4_piece_with_location_4_then_diceBox_enableDiceWith_4_times_must_be_called() {
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(4)).enableDiceWith(4)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_4_4_and_all_pieces_locations_is_1_then_diceBox_enableDiceWith4_atLeast_4_times_must_be_called() {
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(4)).enableDiceWith(4)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_4_4_and_all_pieces_locations_are_1_except_one_which_is_5_and_board_canMovePiece_is_true_every_time_then_diceBox_enableDiceWith4_atLeast_4_times_must_be_called() {
        lst[0].location = 5
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(4, 4, 4, 4))
        `when`(board.canMovePiece(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true)
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(4)).enableDiceWith(4)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_board_canMovePiece_with_1_is_false_then_diceBox_enableDiceWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        lst.forEach {
            it.location = 2
        }
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(1)).enableDiceWith(4)
        verify(diceBox, never()).enableDiceWith(1)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_1_2_and_all_pieces_locations_are_4_and_board_canMovePiece_with_1_3_are_false_then_diceBox_enableDiceWith_2_atLeast_1_time_must_be_called_but_1_never_called() {
        lst.forEach {
            it.location = 4
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
            `when`(board.canMovePiece(it, it.pieceAfterMove(2))).thenReturn(true)
//            `when`(board.canMovePiece(it, it.pieceAfterMove(3))).thenReturn(false)
        }
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(1, 2))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(1)).enableDiceWith(2)
        verify(diceBox, never()).enableDiceWith(1)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_1_4_and_all_pieces_locations_is_2_and_board_canMovePiece_with1_is_false_then_diceBox_enableDiceWith_4_atLeast_1_time_must_be_called_but_1_never_called() {
        lst.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(1)).enableDiceWith(4)
        verify(diceBox, never()).enableDiceWith(1)
    }

    @Test
    fun given_updateDiceBoxStatus_called_when_dices_numbers_are_1_4_and_all_pieces_locations_is_2_except_one_with_location_3_and_board_canMovePiece_with1_is_false_then_diceBox_enableDiceWith_4_and_1_atLeast_1_time_must_be_called() {
        lst.forEach {
            it.location = 2
            `when`(board.canMovePiece(it, it.pieceAfterMove(1))).thenReturn(false)
        }
        lst[0].location = 3
        `when`(board.canMovePiece(lst[0], lst[0].pieceAfterMove(1))).thenReturn(true)
        `when`(diceBox.allActiveDicesNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
        verify(diceBox, atLeast(1)).enableDiceWith(4)
        verify(diceBox, atLeast(1)).enableDiceWith(1)
    }

    @Test(expected = ChooseStrategyException::class)
    fun given_updateDiceBoxStatus_called_when_a_piece_is_dead_then_throw_exception() {
        lst[0].state = PieceState.DEAD
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun given_updateDiceBoxStatus_called_when_a_piece_is_not_in_home_range_then_throw_exception() {
        lst[0].location = 14
        playerPiecesActionStrategy.updateDiceBoxStatus(diceBox, lst, board)
    }

    @Test
    fun given_move_called_when_board_move_return_true_then_move_should_be_true() {
        val piece = lst[14]
        val dice = mock(Dice::class.java)
        `when`(dice.number).thenReturn(4)
        `when`(board.move(piece, 4)).thenReturn(true)
        val move = playerPiecesActionStrategy.move(dice, piece, board)
        assertTrue(move)
    }


    @Test(expected = CellNumberException::class)
    fun given_findDice_called_when_both_of_startCell_and_destinationCell_are_null_then_throw_CellNumberException() {
        playerPiecesActionStrategy.findDice(null, null, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun given_findDice_called_when_startCell_is_null_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(null, 8, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun given_findDice_called_when_startCell_is_not_in_DiceRange_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(13, 8, diceBox, board)
    }

    @Test
    fun given_findDice_called_when_startCell_is_6_and_destinationCell_is_5_and_board_findDistanceBetweenTwoCell_return1_then_diceBox_getActiveDiceWithNumber_1_should_be_called() {
        `when`(board.findDistanceBetweenTwoCell(6, 5)).thenReturn(1)
        playerPiecesActionStrategy.findDice(6, 5, diceBox, board)
        verify(diceBox).getActiveDiceWithNumber(1)
    }

    @Test
    fun given_findDice_called_when_startCell_is_2_and_destinationCell_is_null_then_diceBox_getDiceGreaterEqual_2_should_be_called() {
        playerPiecesActionStrategy.findDice(2, null, diceBox, board)
        verify(diceBox).getActiveDiceGreaterEqual(2)
    }

    @Test
    fun given_findDice_called_when_startCell_is_6_and_destinationCell_is_6_then_return_null() {
        val result = playerPiecesActionStrategy.findDice(6, 6, diceBox, board)
        assertTrue(result == null)
    }
}