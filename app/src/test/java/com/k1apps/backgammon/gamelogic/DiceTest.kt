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

    @Test(expected = DiceException::class)
    fun given_roll_called_when_dice_rolled_and_not_yet_use_and_rolled_again_then_throw_DiceException() {
        dice.roll()
        //dice.enable()
        dice.roll()
    }

    @Test
    fun given_roll_called_then_number_should_between_1_to_6() {
        dice.roll()
        assertTrue(dice.number!! in 1..6)
    }

    @Test(expected = DiceException::class)
    fun give_use_called_when_dice_is_not_yet_rolled_then_throw_DiceException() {
        dice.use()
    }

    @Test
    fun give_use_called_when_dice_is_disable_then_return_false() {
        dice.roll()
        //dice.enable()
        assertFalse(dice.use())
    }

    @Test
    fun give_use_called_when_dice_rolled_and_enabled_then_return_value_should_be_true_and_can_roll_again() {
        dice.roll()
        dice.enable()
        assertTrue(dice.use())
        dice.roll()
    }

    @Test(expected = DiceException::class)
    fun give_use_called_when_dice_rolled_and_enabled_and_twice_then_return_value_should_be_true_and_can_not_roll_again() {
        dice.roll()
        dice.enable()
        dice.twice()
        assertTrue(dice.use())
        dice.roll()
    }

    @Test(expected = DiceException::class)
    fun give_enable_called_when_dice_is_not_yet_rolled_then_throw_DiceException() {
        dice.enable()
    }

    @Test(expected = DiceException::class)
    fun given_twice_called_when_dice_is_not_rolled_then_throw_DiceException() {
        dice.twice()
    }

    @Test(expected = DiceException::class)
    fun given_twice_called_when_dice_is_used_before_then_throw_DiceException() {
        dice.roll()
        dice.enable()
        dice.use()
        dice.twice()
    }

    @Test
    fun given_isActive_called_when_dice_is_not_yet_rolled_then_return_false() {
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_rolled_and_not_enabled_then_return_false() {
        dice.roll()
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_rolled_and_enabled_and_used_then_return_false() {
        dice.roll()
        dice.enable()
        dice.use()
        assertFalse(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_rolled_and_enabled_and_twice_and_used_then_return_true() {
        dice.roll()
        dice.enable()
        dice.twice()
        dice.use()
        assertTrue(dice.isActive())
    }

    @Test
    fun given_isActive_called_when_rolled_and_enabled_then_return_true() {
        dice.roll()
        dice.enable()
        assertTrue(dice.isActive())
    }
}