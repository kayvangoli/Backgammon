package com.k1apps.backgammon.gamelogic

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class DiceTest {

    @Spy
    var dice: Dice = spy(DiceImpl(Random))

    @Test
    fun when_dice_every_time_roll_called_then_dice_should_be_disabled() {
        dice.roll()
        Mockito.verify(dice, times(1)).enabled = false
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

}