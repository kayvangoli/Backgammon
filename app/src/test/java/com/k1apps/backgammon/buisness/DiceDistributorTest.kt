package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.DaggerTurnaroundComponentTest
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class DiceDistributorTest {

    @Inject
    lateinit var diceDistributorImpl: DiceDistributorImpl
    @Inject
    lateinit var diceBox: DiceBox

    @Mock
    lateinit var player1: Player

    @Mock
    lateinit var player2: Player

    @Before
    fun setup() {
        DaggerTurnaroundComponentTest.create().inject(this)
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun when_player1_dice_num_is_6_and_player2_is_2_then_set_dice_box_to_player1_and_retake_dice() {
        diceDistributorImpl.onEvent(DiceThrownEvent(player1, 6))
        diceDistributorImpl.onEvent(DiceThrownEvent(player2, 2))
        verify(player1, times(1)).diceBox = diceBox
        verify(player2, times(0)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()

    }

    @Test
    fun when_player1_dice_num_is_1_and_player2_is_4_then_set_dice_box_to_player2_and_retake_dice() {
        diceDistributorImpl.onEvent(DiceThrownEvent(player1, 1))
        diceDistributorImpl.onEvent(DiceThrownEvent(player2, 4))
        verify(player1, times(0)).diceBox = diceBox
        verify(player2, times(1)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()
    }

    @Test
    fun when_player1_dice_num_is_equal_to_player2_then_hold_dice_box_and_dont_retake_dice() {
        diceDistributorImpl.onEvent(DiceThrownEvent(player1, 5))
        diceDistributorImpl.onEvent(DiceThrownEvent(player2, 5))
        verify(player1, times(0)).diceBox = diceBox
        verify(player2, times(0)).diceBox = diceBox
        verify(player1, times(0)).retakeDice()
        verify(player2, times(0)).retakeDice()
    }

    @Test
    fun when_player1_has_dice_box_then_return_player1_on_whichPlayerHasDiceBox() {
        diceDistributorImpl.onEvent(DiceThrownEvent(player1, 6))
        diceDistributorImpl.onEvent(DiceThrownEvent(player2, 5))
        Mockito.`when`(player1.diceBox).thenReturn(mock(DiceBox::class.java))
        assertTrue(diceDistributorImpl.whichPlayerHasDice() == player1)
    }
    @Test
    fun when_player2_has_dice_box_then_return_player2_on_whichPlayerHasDiceBox() {
        diceDistributorImpl.onEvent(DiceThrownEvent(player1, 5))
        diceDistributorImpl.onEvent(DiceThrownEvent(player2, 6))
        Mockito.`when`(player2.diceBox).thenReturn(mock(DiceBox::class.java))
        assertTrue(diceDistributorImpl.whichPlayerHasDice() == player2)
    }
}