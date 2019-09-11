package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.DaggerPlayerComponentTest
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class PlayerTest {
    @Inject
    lateinit var player: Player
    @Inject
    lateinit var diceRollCallback: DiceRollCallback

    @Before
    fun setup() {
        DaggerPlayerComponentTest.create().inject(this)
    }

    @Test
    fun when_roll_called_with_dice_then_roll_dice_callback_invoked() {
        val diceMock: Dice = mock()
        Mockito.`when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        player.roll()
        verify(diceRollCallback, times(1)).onThrewDice(player, 2)
    }

    @Test
    fun when_retakeDice_called_then_dice_should_be_null() {
        val diceMock: Dice = mock()
        Mockito.`when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }
}