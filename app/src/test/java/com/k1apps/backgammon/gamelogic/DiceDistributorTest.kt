package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.gamelogic.event.DiceThrownEvent
import com.k1apps.backgammon.gamelogic.event.DiceBoxThrownEvent
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class DiceDistributorTest {

    @Mock
    lateinit var diceBox: DiceBox

    @Mock
    lateinit var player1: Player

    @Mock
    lateinit var player2: Player

    lateinit var diceDistributor : DiceDistributor

    @Before
    fun setup() {
        diceDistributor = DiceDistributorImpl(player1, player2, diceBox)
    }

    @Test
    fun when_dice_distributor_started_then_each_player_must_have_dice() {
        val dice1 = mock(Dice::class.java)
        `when`(diceBox.dice1).thenReturn(dice1)
        val dice2 = mock(Dice::class.java)
        `when`(diceBox.dice2).thenReturn(dice2)
        diceDistributor.start()
        verify(player1, times(1)).dice = dice1
        verify(player2, times(1)).dice = dice2
    }

    @Test
    fun given_DiceThrownEvent_when_player1_dice_num_is_6_and_player2_is_2_then_set_dice_box_to_player1_and_retake_dices() {
        `when`(diceBox.dice1.number).thenReturn(6)
        `when`(diceBox.dice2.number).thenReturn(2)
        diceDistributor.onEvent(DiceThrownEvent(player1))
        diceDistributor.onEvent(DiceThrownEvent(player2))
        verify(player1, times(1)).diceBox = diceBox
        verify(player2, times(0)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()

    }

    @Test
    fun when_player1_dice_num_is_1_and_player2_is_4_then_set_dice_box_to_player2_and_retake_dice() {
        diceDistributor.start()
        `when`(diceBox.dice1.number).thenReturn(1)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceDistributor.onEvent(DiceThrownEvent(player2))
        diceDistributor.onEvent(DiceThrownEvent(player1))
        verify(player1, times(0)).diceBox = diceBox
        verify(player2, times(1)).diceBox = diceBox
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()
    }

    @Test
    fun when_player1_dice_num_is_equal_to_player2_then_retake_dices_and_restart_roll_dices() {
        `when`(diceBox.dice1.number).thenReturn(2)
        `when`(diceBox.dice2.number).thenReturn(2)
        diceDistributor.onEvent(DiceThrownEvent(player1))
        diceDistributor.onEvent(DiceThrownEvent(player2))
        verify(player1, times(1)).dice = diceBox.dice1
        verify(player2, times(1)).dice = diceBox.dice2
        verify(player1, times(1)).retakeDice()
        verify(player2, times(1)).retakeDice()
    }

    @Test
    fun when_player1_has_dice_box_then_return_player1_on_whichPlayerHasDiceBox() {
        player1.diceBox = diceBox
        assertTrue(diceDistributor.whichPlayerHasDice()?.first == player1)
    }

    @Test
    fun when_player2_has_dice_box_then_return_player2_on_whichPlayerHasDiceBox() {
        player2.diceBox = diceBox
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

    @Test
    fun when_dice_box_thrown_then_invoke_player_update_dice() {
        val player = mock(Player::class.java)
        `when`(diceBox.isAtLeastOneDiceEnable()).thenReturn(true)
        `when`(player.diceBox).thenReturn(diceBox)
        val diceBoxThrownEvent = DiceBoxThrownEvent(player)
        diceDistributor.onEvent(diceBoxThrownEvent)
        verify(player, times(1)).updateDicesStateInDiceBox()
    }

    @Test
    fun when_dice_box_thrown_and_after_player_updated_dice_box_to_disable_then_retake_dice_box_from_player_set_dice_box_to_opponent() {
        val diceBoxThrownEvent = DiceBoxThrownEvent(player1)
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        player1.diceBox = diceBox
        `when`(diceBox.isAtLeastOneDiceEnable()).thenReturn(false)
        diceDistributor.onEvent(diceBoxThrownEvent)
        verify(player1, times(1)).retakeDiceBox()
        verify(player2, times(1)).diceBox = diceBox
    }

//    @Test
//    fun given_whichPlayerHasDiceBox_when_player1_has_diceBox_then_return_player1() {
//        player1.diceBox = diceBox
//        assertTrue(diceDistributor.whichPlayerHasDiceBox() === player1)
//    }

}