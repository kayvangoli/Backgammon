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
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

class PlayerIsInGamePieceStrategyTest {

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
        DaggerPlayerIsInGamePieceComponentTest.builder()
            .setBoardModule(SpyBoardModule())
            .setDiceBoxModule(SpyDiceBoxModuleTest())
            .setPieceListModule(SpyPieceListModule())
            .build()
            .inject(this)
    }


    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_with_default_arrangement_then_diceBox_updateDiceStateWith_4_and_1_atLeast_1_time_must_be_called() {
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_with_default_arrangement_then_diceBox_updateDiceStateWith_4_and_1_atLeast_1_time_must_be_called() {
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(1)
    }

    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_and_all_move_with_4_is_filled_by_opponent_then_diceBox_updateDiceStateWith_1_atLeast_1_time_must_be_called_but_1_never_called() {
        for (piece in normalPieceList) {
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(4))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_and_all_move_with_4_is_filled_by_opponent_then_diceBox_updateDiceStateWith_1_atLeast_1_time_must_be_called_but_1_never_called() {
        for (piece in reversePieceList) {
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(4))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, atLeastOnce()).updateDiceStateWith(1)
    }
    @Test
    fun when_updateDicesState_called_and_dices_numbers_are_1_4_and_all_move_with_4_and_1_are_filled_by_opponent_then_diceBox_updateDiceStateWith_1_and_4_never_called() {
        for (piece in normalPieceList) {
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(4))).thenReturn(false)
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, normalPieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }

    @Test
    fun when_reverse_updateDicesState_called_and_dices_numbers_are_1_4_and_all_move_with_4_and_1_are_filled_by_opponent_then_diceBox_updateDiceStateWith_1_and_4_never_called() {
        for (piece in reversePieceList) {
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(4))).thenReturn(false)
            `when`(board.canMovePiece(piece, piece.pieceAfterMove(1))).thenReturn(false)
        }
        `when`(diceBox.getAllUnUsedNumbers()).thenReturn(arrayListOf(1, 4))
        playerPiecesActionStrategy.updateDicesState(diceBox, reversePieceList, board)
        verify(diceBox, never()).updateDiceStateWith(4)
        verify(diceBox, never()).updateDiceStateWith(1)
    }
}

@GameScope
@Component(
    modules = [
        PlayerInGamePieceModuleTest::class,
        PieceListModule::class,
        BoardModule::class,
        DiceBoxModule::class
    ]
)
interface PlayerIsInGamePieceComponentTest {
    @Component.Builder
    interface Builder {
        fun setPieceListModule(pieceListModule: PieceListModule): Builder
        fun setBoardModule(boardModule: BoardModule): Builder
        fun setDiceBoxModule(diceBoxModule: DiceBoxModule): Builder
        fun build(): PlayerIsInGamePieceComponentTest
    }
    fun inject(playerIsInGamePieceStrategyTest: PlayerIsInGamePieceStrategyTest)
}

@Module
class PlayerInGamePieceModuleTest {
    @Provides
    fun providePlayerInPieceStrategy(): PlayerPiecesActionStrategy {
        return PlayerIsInGamePieceStrategy()
    }
}