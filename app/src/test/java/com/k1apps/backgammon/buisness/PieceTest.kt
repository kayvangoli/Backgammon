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
    fun when_piece_reverse_set_location_1_then_location_must_be_24() {
        pieceReverse.location = 1
        assertTrue("piece location is: ${pieceReverse.location}", pieceReverse.location == 24)
    }

    @Test
    fun when_piece_normal_set_location_1_then_location_must_be_1() {
        pieceNormal.location = 1
        assertTrue("piece location is: ${pieceNormal.location}", pieceNormal.location == 1)
    }

    @Test
    fun when_pieceAfterMove_for_each_piece_type_with_number_2_called_and_piece_is_dead_then_return_piece_with_location_2{
        
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