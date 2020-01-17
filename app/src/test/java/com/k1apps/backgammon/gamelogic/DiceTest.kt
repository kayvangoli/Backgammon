package com.k1apps.backgammon.gamelogic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class DiceTest {

    @Spy
    val random = Random
    @Spy
    var dice: Dice = spy(DiceImpl(random))

    @Test
    fun when_dice_every_time_roll_called_then_dice_should_be_disabled() {
        dice.roll()
        verify(dice, times(1)).enabled = false
    }

    @Test
    fun when_dice_roll_called_then_number_should_between_1_to_6() {
        dice.roll()
        assertTrue(dice.number!! in 1..6)
    }

    @Test
    fun when_dice_roll_not_yet_called_then_number_should_be_null() {
        assertTrue(dice.number == null)
    }

    @Test
    fun given_isActive_called_when_used_is_true_and_enable_is_true_then_return_false() {
        dice.enabled = true
        dice.used = true
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_used_is_false_and_enable_is_false_then_return_false() {
        dice.enabled = false
        dice.used = false
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_used_is_true_and_enable_is_false_then_return_false() {
        dice.used = true
        dice.enabled = false
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_used_is_false_and_enable_is_true_then_return_true() {
        dice.enabled = true
        dice.used = false
        assertTrue(dice.isActive())
    }

    @Test
    fun given_copy_called_when_number_is_3_then_return_dice_with_number_3() {
        `when`(dice.number).thenReturn(3)
        val copiedDice = dice.copy()
        assertTrue(copiedDice!!.number == 3.toByte())
    }

}