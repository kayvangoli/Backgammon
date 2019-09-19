package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.REVERSE_PLAYER
import com.k1apps.backgammon.dagger.*
import dagger.Component
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
        check(0, 1, 24)
    }

    @Test
    fun when_index_is_1_then_location1_must_be_1_and_location2_must_be_24() {
        check(1, 1, 24)
    }

    @Test
    fun when_index_is_2_to_6_then_location1_must_be_12_and_on_location2_must_be_13() {
        check(2, 12, 13)
        check(3, 12, 13)
        check(4, 12, 13)
        check(5, 12, 13)
        check(6, 12, 13)
    }

    @Test
    fun when_index_is_7_to_9_then_location1_must_be_17_and_on_location2_must_be_8() {
        check(7, 17, 8)
        check(8, 17, 8)
        check(9, 17, 8)
    }

    @Test
    fun when_index_is_10_to_14_then_location1_must_be_19_and_on_location2_must_be_6() {
        check(10, 19, 6)
        check(11, 19, 6)
        check(12, 19, 6)
        check(13, 19, 6)
        check(14, 19, 6)
    }

    @Test
    fun when_board_init_is_called_then_player1_piece_with_index_0_location_should_be_1() {
        assertTrue("player1 piece location with index 0 is ${player1.pieceList[0].location}",
            player1.pieceList[0].location == 1)
    }
    @Test
    fun when_board_init_is_called_then_player1_piece_with_index_2_location_should_be_12() {
        assertTrue("player1 piece location with index 2 is ${player1.pieceList[2].location}",
            player1.pieceList[2].location == 12)
    }

    @Test
    fun when_board_init_is_called_then_player2_piece_with_index_0_location_should_be_24() {
        assertTrue("player1 piece location with index 0 is ${player2.pieceList[0].location}",
            player2.pieceList[0].location == 24)
    }
    @Test
    fun when_board_init_is_called_then_player2_piece_with_index_2_location_should_be_13() {
        assertTrue("player1 piece location with index 2 is ${player2.pieceList[2].location}",
            player2.pieceList[2].location == 13)
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
}