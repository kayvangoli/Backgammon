package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.GameScope
import dagger.Component
import dagger.Module
import dagger.Provides
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.abs

class PieceTest {
    @Inject
    @field:Named("normal")
    lateinit var pieceNormal: Piece

    @Inject
    @field:Named("reverse")
    lateinit var pieceReverse: Piece

    @Before
    fun setup() {
        DaggerPieceComponentTest.create().inject(this)
    }

    @Test
    fun when_piece_set_location_is_greater_24_or_less_than_1_then_location_must_not_changed() {
        pieceNormal.location = 6
        pieceNormal.location = 28
        assertTrue(pieceNormal.location == 6)
        pieceNormal.location = 0
        assertTrue(pieceNormal.location == 6)

        pieceReverse.location = 1
        pieceReverse.location = 28
        assertTrue(pieceReverse.location == 24)
        pieceReverse.location = 0
        assertTrue(pieceReverse.location == 24)
    }

    @Test
    fun when_piece_reverse_set_location_1_to_24_then_location_must_be_24_to_1() {
        for (index in 1..24) {
            pieceReverse.location = index
            assertTrue(
                "piece location is: ${pieceReverse.location}",
                pieceReverse.location == abs(index - 25)
            )
        }
    }

    @Test
    fun when_piece_normal_set_location_1_to_24_then_location_must_be_1_to_24() {
        for (index in 1..24) {
            pieceNormal.location = index
            assertTrue("piece location is: ${pieceNormal.location}", pieceNormal.location == index)
        }
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_1_called_and_piece_is_dead_then_return_dead_piece_with_location_1() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(1)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 1)
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_2_called_and_piece_is_dead_then_return_dead_piece_with_location_2() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(2)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 2)
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_3_called_and_piece_is_dead_then_return_dead_piece_with_location_3() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(3)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 3)
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_4_called_and_piece_is_dead_then_return_dead_piece_with_location_4() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(4)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 4)
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_5_called_and_piece_is_dead_then_return_dead_piece_with_location_5() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(5)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 5)
    }

    @Test
    fun when_pieceAfterMove_for_reverse_piece_type_with_number_6_called_and_piece_is_dead_then_return_dead_piece_with_location_6() {
        pieceReverse.state = PieceState.DEAD
        val pieceAfterMove = pieceReverse.pieceAfterMove(6)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue("location is ${pieceAfterMove.location}", pieceAfterMove.location == 6)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_1_called_and_piece_is_dead_then_return_dead_piece_with_location_24() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(1)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 24)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_2_called_and_piece_is_dead_then_return_dead_piece_with_location_23() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(2)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 23)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_3_called_and_piece_is_dead_then_return_dead_piece_with_location_22() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(3)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 22)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_4_called_and_piece_is_dead_then_return_dead_piece_with_location_21() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(4)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 21)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_5_called_and_piece_is_dead_then_return_dead_piece_with_location_20() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(5)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 20)
    }

    @Test
    fun when_pieceAfterMove_for_normal_piece_type_with_number_6_called_and_piece_is_dead_then_return_dead_piece_with_location_19() {
        pieceNormal.state = PieceState.DEAD
        val pieceAfterMove = pieceNormal.pieceAfterMove(6)
        assertTrue(pieceAfterMove.state == PieceState.DEAD)
        assertTrue(pieceAfterMove.location == 19)
    }


    @Test
    fun when_pieceAfterMove_for_each_piece_type_called_then_return_piece_with_different_address() {
        assertTrue(pieceNormal.pieceAfterMove(2) !== pieceNormal)
        assertTrue(pieceReverse.pieceAfterMove(2) !== pieceReverse)
    }

    @Test
    fun when_pieceAfterMove_for_each_piece_type_won_called_then_return_piece_exactly_like_main_piece() {
        (1..7).forEach { index ->
            pieceNormal.state = PieceState.WON
            val normalAfterMove = pieceNormal.pieceAfterMove(index.toByte())
            assertTrue(normalAfterMove == pieceNormal)

            pieceReverse.state = PieceState.WON
            val reverseAfterMove = pieceReverse.pieceAfterMove(index.toByte())
            assertTrue(reverseAfterMove == pieceReverse)
        }
    }
}

@GameScope
@Component(modules = [PieceModuleTest::class])
interface PieceComponentTest {
    fun inject(pieceTest: PieceTest)
}

@Module
class PieceModuleTest {
    @GameScope
    @Provides
    @Named("reverse")
    fun providePiece1(): Piece {
        return PieceImpl(MoveType.Revers)
    }

    @GameScope
    @Provides
    @Named("normal")
    fun providePiece2(): Piece {
        return PieceImpl(MoveType.Normal)
    }
}