package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.DaggerDiceDistributorComponentTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import javax.inject.Inject
import javax.inject.Named

class DiceDistributorTest {

    @Inject
    @field:Named("mockPlayer")
    lateinit var diceDistributorMockPlayer: DiceDistributorImpl

    @Inject
    @field:Named("realPlayer")
    lateinit var diceDistributorRealPlayer: DiceDistributorImpl

    @Inject
    lateinit var diceBox: DiceBox

    @Inject
    @field:Named("normalMockPlayer")
    lateinit var mockPlayer1: Player

    @Inject
    @field:Named("reverseMockPlayer")
    lateinit var mockPlayer2: Player

    @Inject
    @field:Named("normalPlayer")
    lateinit var player1: Player

    @Inject
    @field:Named("reversePlayer")
    lateinit var player2: Player

    @Before
    fun setup() {
        DaggerDiceDistributorComponentTest.create().inject(this)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun when_player1_dice_num_is_6_and_player2_is_2_then_set_dice_box_to_player1_and_retake_dice() {
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer1, 6))
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer2, 2))
        verify(mockPlayer1, times(1)).diceBox = diceBox
        verify(mockPlayer2, times(0)).diceBox = diceBox
        verify(mockPlayer1, times(1)).retakeDice()
        verify(mockPlayer2, times(1)).retakeDice()

    }

    @Test
    fun when_player1_dice_num_is_1_and_player2_is_4_then_set_dice_box_to_player2_and_retake_dice() {
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer1, 1))
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer2, 4))
        verify(mockPlayer1, times(0)).diceBox = diceBox
        verify(mockPlayer2, times(1)).diceBox = diceBox
        verify(mockPlayer1, times(1)).retakeDice()
        verify(mockPlayer2, times(1)).retakeDice()
    }

    @Test
    fun when_player1_dice_num_is_equal_to_player2_then_hold_dice_box_and_dont_retake_dice() {
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer1, 5))
        diceDistributorMockPlayer.onEvent(DiceThrownEvent(mockPlayer2, 5))
        verify(mockPlayer1, times(0)).diceBox = diceBox
        verify(mockPlayer2, times(0)).diceBox = diceBox
        verify(mockPlayer1, times(0)).retakeDice()
        verify(mockPlayer2, times(0)).retakeDice()
    }

    @Test
    fun when_player1_has_dice_box_then_return_player1_on_whichPlayerHasDiceBox() {
        diceDistributorRealPlayer.onEvent(DiceThrownEvent(player1, 6))
        diceDistributorRealPlayer.onEvent(DiceThrownEvent(player2, 5))
        assertTrue(diceDistributorRealPlayer.whichPlayerHasDice() == player1)
    }

    @Test
    fun when_player2_has_dice_box_then_return_player2_on_whichPlayerHasDiceBox() {
        diceDistributorRealPlayer.onEvent(DiceThrownEvent(player1, 5))
        diceDistributorRealPlayer.onEvent(DiceThrownEvent(player2, 6))
        assertTrue(diceDistributorRealPlayer.whichPlayerHasDice() == player2)
    }
}