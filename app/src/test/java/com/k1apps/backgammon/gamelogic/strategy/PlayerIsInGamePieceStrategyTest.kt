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
import org.junit.Assert
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

    @Test(expected = ChooseStrategyException::class)
    fun when_move_called_and_destination_cell_is_out_of_board_then_thrown_chooseStrategyException() {
        val piece = normalPieceList[0]
        piece.location = 2
        `when`(diceBox.dice1.number).thenReturn(3)
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
        Assert.assertTrue(move)
    }

    @Test
    fun when_move_called_with_reverse_piece_then_move_should_be_true_and_board_move_called() {
        val piece = reversePieceList[14]
        `when`(diceBox.dice1.number).thenReturn(4)
        val move = playerPiecesActionStrategy.move(diceBox.dice1, piece, board)
        verify(board).move(piece, diceBox.dice1.number!!)
        Assert.assertTrue(move)
    }

    @Test(expected = CellNumberException::class)
    fun when_findDice_called_and_both_of_fromCell_and_toCell_are_null_then_throw_CellNumberException() {
        playerPiecesActionStrategy.findDice(null, null, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_findDice_called_and_fromCell_is_null_and_toCell_is_8_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(null, 8, diceBox, board)
    }

    @Test(expected = ChooseStrategyException::class)
    fun when_findDice_called_and_fromCell_is_8_and_toCell_is_null_then_throw_ChooseStrategyException() {
        playerPiecesActionStrategy.findDice(8, null, diceBox, board)
    }

    @Test
    fun when_findDice_called_and_fromCell_is_6_and_to_cell_is_5_then_diceBox_getDiceWithNumber_1_and_board_findDistanceBetweenTwoCell_should_be_called() {
        val mockDiceBox = mock(DiceBox::class.java)
        playerPiecesActionStrategy.findDice(6, 5, mockDiceBox, board)
        verify(board).findDistanceBetweenTwoCell(6, 5)
        verify(mockDiceBox).getActiveDiceWithNumber(1)
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