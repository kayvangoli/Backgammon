package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.REVERSE_PLAYER
import com.k1apps.backgammon.dagger.*
import dagger.Component
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@GameScope
@Component(
    modules = [GameModule::class, PlayerModule::class, DiceBoxModule::class, PieceListModule::class]
)
interface BoardComponentTest {
    fun inject(boardTest: BoardTest)
}


class BoardTest {

    @Inject
    @field:Named(NORMAL_PLAYER)
    lateinit var player1: Player

    @Inject
    @field:Named(REVERSE_PLAYER)
    lateinit var player2: Player

    @Inject
    lateinit var board: Board

    @Before
    fun setup() {
        DaggerBoardComponentTest.create().inject(this)
        board.initBoard()
    }

    @Test
    fun list_size_is_15() {
        assertTrue("list1 size is ${board.pieceList1.size}", board.pieceList1.size == 15)
        assertTrue("list2 size is ${board.pieceList2.size}", board.pieceList2.size == 15)
    }

    @Test
    fun when_index_is_0_then_location1_must_be_1_and_location2_must_be_24() {
        check(0, 24, 1)
    }

    @Test
    fun when_index_is_1_then_location1_must_be_1_and_location2_must_be_24() {
        check(1, 24, 1)
    }

    @Test
    fun when_index_is_2_to_6_then_location1_must_be_12_and_on_location2_must_be_13() {
        check(2, 13, 12)
        check(3, 13, 12)
        check(4, 13, 12)
        check(5, 13, 12)
        check(6, 13, 12)
    }

    @Test
    fun when_index_is_7_to_9_then_location1_must_be_17_and_on_location2_must_be_8() {
        check(7, 8, 17)
        check(8, 8, 17)
        check(9, 8, 17)
    }

    @Test
    fun when_index_is_10_to_14_then_location1_must_be_19_and_on_location2_must_be_6() {
        check(10, 6, 19)
        check(11, 6, 19)
        check(12, 6, 19)
        check(13, 6, 19)
        check(14, 6, 19)
    }

    @Test
    fun when_board_init_is_called_then_player2_piece_with_index_0_location_should_be_1() {
        assertTrue(
            "player1 piece location with index 0 is ${player1.pieceList[0].location}",
            player2.pieceList[0].location == 1
        )
    }

    @Test
    fun when_board_init_is_called_then_player2_piece_with_index_2_location_should_be_12() {
        assertTrue(
            "player1 piece location with index 2 is ${player1.pieceList[2].location}",
            player2.pieceList[2].location == 12
        )
    }

    @Test
    fun when_board_init_is_called_then_player1_piece_with_index_0_location_should_be_24() {
        assertTrue(
            "player1 piece location with index 0 is ${player2.pieceList[0].location}",
            player1.pieceList[0].location == 24
        )
    }

    @Test
    fun when_board_init_is_called_then_player1_piece_with_index_2_location_should_be_13() {
        assertTrue(
            "player1 piece location with index 2 is ${player2.pieceList[2].location}",
            player1.pieceList[2].location == 13
        )
    }

    private fun check(index: Int, location1: Int, location2: Int) {
        assertTrue(
            "location is: ${board.pieceList1[index].location}",
            board.pieceList1[index].location == location1
        )
        assertTrue(
            "location is: ${board.pieceList2[index].location}",
            board.pieceList2[index].location == location2
        )
    }

    @Test
    fun when_isRangeFilledWithNormalPiece_called_with_default_arrangement_pieces_then_return_false() {
        assertFalse(board.isRangeFilledWithNormalPiece(player1.homeCellIndexRange))
    }

    @Test
    fun when_isRangeFilledWithReversePiece_called_with_default_arrangement_pieces_then_return_false() {
        assertFalse(board.isRangeFilledWithReversePiece(player2.homeCellIndexRange))
    }

    @Test
    fun when_isRangeFilledWithNormalPiece_called_with_filled_range_piece_list_then_return_true() {
        val arrangements: ArrayList<ArrangementConfig> = arrayListOf()
        player1.homeCellIndexRange.forEach {
            arrangements.add(ArrangementConfig(2, it))
        }
        pieceListArrangement(board.pieceList1, ArrangementListConfig(arrangements))
        pieceListArrangementInOneLocation(board.pieceList2, 15)
        board.initBoard()
        assertTrue(board.isRangeFilledWithNormalPiece(player1.homeCellIndexRange))
    }

    @Test
    fun when_isRangeFilledWithReversePiece_called_with_filled_range_piece_list_then_return_true() {
        val arrangements: ArrayList<ArrangementConfig> = arrayListOf()
        player2.homeCellIndexRange.forEach {
            arrangements.add(ArrangementConfig(2, it))
        }
        pieceListArrangement(board.pieceList2, ArrangementListConfig(arrangements))
        pieceListArrangementInOneLocation(board.pieceList1, 1)
        board.initBoard()
        assertTrue(board.isRangeFilledWithReversePiece(player2.homeCellIndexRange))
    }

    @Test
    fun when_canMovePiece_called_with_default_arrangement_and_move_normal_piece_from_cell_12_to_7_then_return_true() {
        val piece = board.pieceList1[3]
        assertTrue(board.canMovePiece(piece, piece.pieceAfterMove(5)))
    }

    @Test
    fun when_canMovePiece_called_with_default_arrangement_and_move_reverse_piece_from_cell_12_to_13_then_return_false() {
        val piece = board.pieceList2[2]
        assertFalse(board.canMovePiece(piece, piece.pieceAfterMove(1)))
    }

    @Test
    fun when_movePiece_called_with_default_arrangement_and_second_index_move_normal_piece_with_number_2_then_piece_location_should_be_11() {
        val piece = board.pieceList1[2]
        val move = board.move(piece, 2)
        assertTrue(piece.location == 11)
        assertTrue(move)
    }

    @Test
    fun when_movePiece_called_with_default_arrangement_and_third_move_reverse_piece_with_number_5_then_piece_location_should_be_17() {
        val piece = board.pieceList2[3]
        val move = board.move(piece, 5)
        assertTrue(piece.location == 17)
        assertTrue(move)
    }

    @Test
    fun when_movePiece_called_with_default_arrangement_and_one_opponent_piece_location_is_11_and_my_piece_index_is_2_and_number_is_2_then_opponent_should_be_killed_and_my_piece_location_should_be_11_and_move_true() {
        val opponentPiece = board.pieceList2[0]
        opponentPiece.location = 11
        board.initBoard()
        val myPiece = board.pieceList1[2]
        val move = board.move(myPiece, 2)
        assertTrue(opponentPiece.state == PieceState.DEAD)
        assertTrue(myPiece.location == 11)
        assertTrue(move)
    }

    @Test
    fun when_reverse_movePiece_called_with_default_arrangement_and_one_opponent_piece_location_is_14_and_my_piece_index_is_2_and_number_is_2_then_opponent_should_be_killed_and_my_piece_location_should_be_14_and_move_true() {
        val opponentPiece = board.pieceList1[0]
        opponentPiece.location = 14
        board.initBoard()
        val myPiece = board.pieceList2[2]
        val move = board.move(myPiece, 2)
        assertTrue(opponentPiece.state == PieceState.DEAD)
        assertTrue(myPiece.location == 14)
        assertTrue(move)
    }

    @Test(expected = MoveException::class)
    fun when_move_called_with_number_8_then_throw_exception() {
        val piece = board.pieceList1[0]
        board.move(piece, 8)
    }

    @Test
    fun when_movePiece_called_with_default_arrangement_and_my_piece_index_is_3_and_number_is_1_then_myPiece_location_stay_unchanged_and_move_false() {
        val myPiece = board.pieceList1[3]
        val location = myPiece.location
        val move = board.move(myPiece, 1)
        assertTrue(myPiece.location == location)
        assertFalse(move)
    }

    @Test
    fun when_reverse_movePiece_called_with_default_arrangement_and_my_piece_index_is_3_and_number_is_1_then_myPiece_location_stay_unchanged_and_move_false() {
        val myPiece = board.pieceList2[3]
        val location = myPiece.location
        val move = board.move(myPiece, 1)
        assertTrue(myPiece.location == location)
        assertFalse(move)
    }

    @Test
    fun when_movePiece_called_with_default_arrangement_and_my_piece_index_is_14_and_number_is_6_then_myPiece_state_should_be_won_and_move_true() {
        val myPiece = board.pieceList1[14]
        val move = board.move(myPiece, 6)
        assertTrue(myPiece.state == PieceState.WON)
        assertTrue(move)
    }

    @Test
    fun when_reverse_movePiece_called_with_default_arrangement_and_my_piece_index_is_14_and_number_is_6_then_myPiece_state_should_be_won_and_move_true() {
        val myPiece = board.pieceList2[14]
        val move = board.move(myPiece, 6)
        assertTrue(myPiece.state == PieceState.WON)
        assertTrue(move)
    }

    @Test(expected = MoveException::class)
    fun when_move_called_with_won_piece_then_throw_exception() {
        val piece = board.pieceList1[0]
        piece.state = PieceState.WON
        board.move(piece, 4)
    }
}