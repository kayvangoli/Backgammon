package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.REVERSE_PLAYER
import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.DiceDistributorModule
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PlayerModule
import dagger.Component
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named

@GameScope
@Component(modules = [DiceDistributorModule::class, PlayerModule::class])
interface DiceDistributorComponentTest {
    @Component.Builder
    interface Builder {
        fun setPlayerModule(playerModule: PlayerModule): Builder
        fun build(): DiceDistributorComponentTest
    }

    fun inject(diceDistributorTest: DiceDistributorTest)
}

class SpyPlayerModule : PlayerModule() {
    override fun providePlayer1(pieceList: ArrayList<Piece>, board: Board): Player {
        return spy(super.providePlayer1(pieceList, board))
    }

    override fun providePlayer2(pieceList: ArrayList<Piece>, board: Board): Player {
        return spy(super.providePlayer2(pieceList, board))
    }
}

class DiceDistributorTest {

    @Inject
    lateinit var diceDistributor: DiceDistributor

    @Inject
    lateinit var diceBox: DiceBox

    @Inject
    @field:Named(NORMAL_PLAYER)
    lateinit var player1: Player

    @Inject
    @field:Named(REVERSE_PLAYER)
    lateinit var player2: Player

    @Before
    fun setup() {
        DaggerDiceDistributorComponentTest.builder().setPlayerModule(SpyPlayerModule())
            .build().inject(this)
    }

    @Test
    fun when_dice_distributor_started_then_each_player_must_have_dice() {
        diceDistributor.start()
        verify(player1, times(1)).dice = diceBox.dice1
        verify(player2, times(1)).dice = diceBox.dice2
    }

    @Test
    fun when_player1_dice_num_is_6_and_player2_is_2_then_set_dice_box_to_player1_and_retake_dice() {
        diceDistributor.onEvent(DiceThrownEvent(player1, 6))
        diceDistributor.onEvent(DiceThrownEvent(player2, 2))
        verify(player1, times(1)).diceBox = diceBox
        verify(player2, times(0)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()

    }

    @Test
    fun when_player1_dice_num_is_1_and_player2_is_4_then_set_dice_box_to_player2_and_retake_dice() {
        diceDistributor.onEvent(DiceThrownEvent(player1, 1))
        diceDistributor.onEvent(DiceThrownEvent(player2, 4))
        verify(player1, times(0)).diceBox = diceBox
        verify(player2, times(1)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()
    }

    @Test
    fun when_player1_dice_num_is_equal_to_player2_then_hold_dice_box_and_dont_retake_dice() {
        diceDistributor.onEvent(DiceThrownEvent(player1, 5))
        diceDistributor.onEvent(DiceThrownEvent(player2, 5))
        verify(player1, times(0)).diceBox = diceBox
        verify(player2, times(0)).diceBox = diceBox
        verify(player1, times(0)).retakeDice()
        verify(player2, times(0)).retakeDice()
    }

    @Test
    fun when_player1_has_dice_box_then_return_player1_on_whichPlayerHasDiceBox() {
        diceDistributor.onEvent(DiceThrownEvent(player1, 6))
        diceDistributor.onEvent(DiceThrownEvent(player2, 5))
        assertTrue(diceDistributor.whichPlayerHasDice()?.first == player1)
    }

    @Test
    fun when_player2_has_dice_box_then_return_player2_on_whichPlayerHasDiceBox() {
        diceDistributor.onEvent(DiceThrownEvent(player1, 5))
        diceDistributor.onEvent(DiceThrownEvent(player2, 6))
        assertTrue(diceDistributor.whichPlayerHasDice()?.first == player2)
    }

    @Test
    fun when_whichPlayerHasDice_called_and_both_player_has_dice_then_return_both_players() {
        `when`(player1.dice).thenReturn(mock(Dice::class.java))
        `when`(player2.dice).thenReturn(mock(Dice::class.java))
        val whichPlayerHasDice = diceDistributor.whichPlayerHasDice()
        verify(player1, times(1)).dice
        verify(player2, times(1)).dice
        assertTrue(whichPlayerHasDice!!.first == player1)
        assertTrue(whichPlayerHasDice.second == player2)
    }
}