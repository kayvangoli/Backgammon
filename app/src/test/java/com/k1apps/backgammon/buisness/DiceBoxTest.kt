package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import dagger.Component
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject

class DiceBoxTest {

    @Inject
    lateinit var diceBox: DiceBox

    @Before
    fun setup() {
        DaggerDiceBoxComponentTest.builder().diceBoxModule(DiceBoxModuleTest()).build().inject(this)
    }

    @Test
    fun when_dice_box_roll_called_then_dice1_and_dice2_roll_must_be_called() {
        diceBox.roll()
        verify(diceBox.dice1, times(1)).roll()
        verify(diceBox.dice2, times(1)).roll()
    }

    @Test
    fun when_dice_box_roll_never_called_then_dice1_and_dice2_number_must_be_null_and_disable() {
        assertTrue(diceBox.dice1.number == null)
        assertTrue(diceBox.dice2.number == null)
        assertFalse(diceBox.dice1.enabled)
        assertFalse(diceBox.dice2.enabled)
    }

    @Test
    fun when_dice_roll_called_and_dices_number_is_pair_then_dice3_and_dice4_should_not_be_null() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.roll()
        assertTrue(diceBox.dice3 != null)
        assertTrue(diceBox.dice4 != null)
    }

    @Test
    fun when_dice_roll_called_and_dices_number_is_pair_then_dice3_and_dice4_number_should_be_equal_to_dice2_number() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.roll()
        assertTrue(diceBox.dice3!!.number!! == 4.toByte())
        assertTrue(diceBox.dice4!!.number!! == 4.toByte())
    }

    @Test
    fun when_dice_roll_called_and_dices_number_is_pair_then_dice3_and_dice4_should_be_disable() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.roll()
        assertFalse(diceBox.dice3!!.enabled)
        assertFalse(diceBox.dice4!!.enabled)
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_4_then_dice_with_number_4_should_be_enabled() {
        `when`(diceBox.dice1.number).thenReturn(4)
        diceBox.canUseDiceWith(4)
        verify(diceBox.dice1, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_4_enabled_true_and_dice2_disable_number_4_then_dice2_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(true)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.canUseDiceWith(4)
        verify(diceBox.dice2, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_5_disable_and_dice2_disable_number_4_then_dice2_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(5)
        `when`(diceBox.dice1.enabled).thenReturn(false)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.canUseDiceWith(4)
        verify(diceBox.dice2, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_4_disable_and_dice2_disable_number_4_then_dice1_should_be_enable_and_dice2_no_enable_effect() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(false)
        `when`(diceBox.dice2.number).thenReturn(4)
        `when`(diceBox.dice2.enabled).thenReturn(false)
        diceBox.canUseDiceWith(4)
        verify(diceBox.dice1, times(1)).enabled = true
        verify(diceBox.dice2, times(0)).enabled = true
        verify(diceBox.dice2, times(0)).enabled = false
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_5_disable_and_dice2_disable_number_5_then_dice1_and_dice2_no_enable_effect() {
        `when`(diceBox.dice1.number).thenReturn(5)
        `when`(diceBox.dice1.enabled).thenReturn(false)
        `when`(diceBox.dice2.number).thenReturn(5)
        `when`(diceBox.dice2.enabled).thenReturn(false)
        diceBox.canUseDiceWith(4)
        verify(diceBox.dice1, times(0)).enabled = true
        verify(diceBox.dice1, times(0)).enabled = false
        verify(diceBox.dice2, times(0)).enabled = true
        verify(diceBox.dice2, times(0)).enabled = false
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dices_are_pairs_and_enable_then_dice3_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(true)
        `when`(diceBox.dice2.enabled).thenReturn(true)
        diceBox.roll()
        diceBox.canUseDiceWith(4)
        assertTrue(diceBox.dice3!!.enabled)
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dices_are_pairs_and_enable_and_dice3_is_enable_then_dice3_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice2.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(true)
        `when`(diceBox.dice2.enabled).thenReturn(true)
        diceBox.roll()
        diceBox.dice3!!.enabled = true
        diceBox.canUseDiceWith(4)
        assertTrue(diceBox.dice4!!.enabled)
    }
}

@GameScope
@Component(modules = [DiceBoxModule::class])
interface DiceBoxComponentTest {
    @Component.Builder
    interface Builder {
        fun diceBoxModule(module: DiceBoxModule): Builder
        fun build(): DiceBoxComponentTest
    }
    fun inject(diceBoxTest: DiceBoxTest)
}

class DiceBoxModuleTest : DiceBoxModule() {
    override fun provideDice(): Dice {
        return spy(super.provideDice())
    }
}