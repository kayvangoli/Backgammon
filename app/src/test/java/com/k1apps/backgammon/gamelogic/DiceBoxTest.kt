package com.k1apps.backgammon.gamelogic

import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import dagger.Component
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import kotlin.random.Random

class DiceBoxTest {

    @Inject
    lateinit var diceBox: DiceBox
    @Inject
    lateinit var random: Random

    @Before
    fun setup() {
        DaggerDiceBoxComponentTest.builder().diceBoxModule(SpyDiceBoxModuleTest()).build()
            .inject(this)
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
    fun when_dice_roll_called_and_dices_number_are_pair_then_dice3_and_dice4_should_not_be_null() {
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
        diceBox.updateDiceStateWith(4)
        verify(diceBox.dice1, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_4_enabled_true_and_dice2_disable_number_4_then_dice2_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(true)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.updateDiceStateWith(4)
        verify(diceBox.dice2, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_5_disable_and_dice2_disable_number_4_then_dice2_should_be_enable() {
        `when`(diceBox.dice1.number).thenReturn(5)
        `when`(diceBox.dice1.enabled).thenReturn(false)
        `when`(diceBox.dice2.number).thenReturn(4)
        diceBox.updateDiceStateWith(4)
        verify(diceBox.dice2, times(1)).enabled = true
    }

    @Test
    fun when_can_use_dice_called_with_4_and_dice1_number_is_4_disable_and_dice2_disable_number_4_then_dice1_should_be_enable_and_dice2_no_enable_effect() {
        `when`(diceBox.dice1.number).thenReturn(4)
        `when`(diceBox.dice1.enabled).thenReturn(false)
        `when`(diceBox.dice2.number).thenReturn(4)
        `when`(diceBox.dice2.enabled).thenReturn(false)
        diceBox.updateDiceStateWith(4)
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
        diceBox.updateDiceStateWith(4)
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
        diceBox.updateDiceStateWith(4)
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
        diceBox.updateDiceStateWith(4)
        assertTrue(diceBox.dice4!!.enabled)
    }

    @Test
    fun when_getAllUnusedNumbers_called_and_dices_are_pairs_with_number5_then_return_list_with_four_numbers5() {
        `when`(random.nextInt(1, 7)).thenReturn(5)
        diceBox.roll()
        val allNumbers = diceBox.getAllUnUsedNumbers()
        assertTrue(allNumbers.size == 4)
        allNumbers.forEach {
            assertTrue(it == 5.toByte())
        }

    }

    @Test
    fun when_getAllUnusedNumbers_called_and_dices_are_4_and_5_then_return_list_with_two_numbers_4_and_5() {
        `when`(random.nextInt(1, 7)).thenReturn(4).thenReturn(5)
        diceBox.roll()
        val allNumbers = diceBox.getAllUnUsedNumbers()
        assertTrue(allNumbers.size == 2)
        assertTrue(allNumbers[0] == 4.toByte())
        assertTrue(allNumbers[1] == 5.toByte())
    }

    @Test
    fun given_getActiveDiceWithNumber_2_called_when_dice1_number_is_2_and_is_deactive_and_dice2_number_is_2_and_active_and_dice3_and_dice4_like_to_dice2_then_return_dice2() {
        `when`(diceBox.dice2.copy()).thenReturn(mock(Dice::class.java))
        `when`(random.nextInt(1, 7)).thenReturn(2).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(false)
        `when`(diceBox.dice2.isActive()).thenReturn(true)
        `when`(diceBox.dice3!!.isActive()).thenReturn(true)
        `when`(diceBox.dice4!!.isActive()).thenReturn(true)
        assertTrue(diceBox.getActiveDiceWithNumber(2) == diceBox.dice2)
    }

    @Test
    fun given_getActiveDiceWithNumber_2_called_when_dice1_number_is_2_and_active_and_dice2_number_is_2_and_active_and_dice3_and_dice4_like_to_dice2_then_return_dice1() {
        `when`(diceBox.dice2.copy()).thenReturn(mock(Dice::class.java))
        `when`(random.nextInt(1, 7)).thenReturn(2).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(true)
        `when`(diceBox.dice2.isActive()).thenReturn(true)
        `when`(diceBox.dice3!!.isActive()).thenReturn(true)
        `when`(diceBox.dice4!!.isActive()).thenReturn(true)
        assertTrue(diceBox.getActiveDiceWithNumber(2) == diceBox.dice1)
    }

    @Test
    fun given_getActiveDiceWithNumber_2_called_when_dice1_number_is_2_and_deactive_and_dice2_number_is_2_and_deactive_and_dice3_is_active_and_dice4_like_to_dice3_then_return_dice3() {
        `when`(diceBox.dice2.copy()).thenReturn(mock(Dice::class.java))
        `when`(random.nextInt(1, 7)).thenReturn(2).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(false)
        `when`(diceBox.dice2.isActive()).thenReturn(false)
        `when`(diceBox.dice3!!.isActive()).thenReturn(true)
        `when`(diceBox.dice3!!.number).thenReturn(2)
        `when`(diceBox.dice4!!.isActive()).thenReturn(true)
        `when`(diceBox.dice4!!.number).thenReturn(2)
        assertTrue(diceBox.getActiveDiceWithNumber(2) == diceBox.dice3)
    }

    @Test
    fun given_getActiveDiceWithNumber_2_called_when_dice1_number_is_2_and_deactive_and_dice2_number_is_2_and_deactive_and_dice3_is_deactive_and_dice4_is_active_then_return_dice4() {
        `when`(diceBox.dice2.copy()).thenReturn(mock(Dice::class.java))
        `when`(random.nextInt(1, 7)).thenReturn(2).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(false)
        `when`(diceBox.dice2.isActive()).thenReturn(false)
        `when`(diceBox.dice3!!.isActive()).thenReturn(false)
        `when`(diceBox.dice3!!.number).thenReturn(2)
        `when`(diceBox.dice4!!.isActive()).thenReturn(true)
        `when`(diceBox.dice4!!.number).thenReturn(2)
        assertTrue(diceBox.getActiveDiceWithNumber(2) == diceBox.dice4)
    }

    @Test
    fun given_getActiveDiceWithNumber_2_called_when_dice1_number_is_2_and_deactive_and_dice2_number_is_2_and_deactive_and_dice3_is_deactive_and_dice4_is_deactive_then_return_null() {
        `when`(diceBox.dice2.copy()).thenReturn(mock(Dice::class.java))
        `when`(random.nextInt(1, 7)).thenReturn(2).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(false)
        `when`(diceBox.dice2.isActive()).thenReturn(false)
        `when`(diceBox.dice3!!.isActive()).thenReturn(false)
        `when`(diceBox.dice4!!.isActive()).thenReturn(false)
        assertTrue(diceBox.getActiveDiceWithNumber(2) == null)
    }

    @Test
    fun given_getActiveDiceWithNumber_5_called_when_dice1_number_is_5_and_deactive_and_dice2_is_2_and_active_then_return_null() {
        `when`(random.nextInt(1, 7)).thenReturn(5).thenReturn(2)
        diceBox.roll()
        `when`(diceBox.dice1.isActive()).thenReturn(false)
        `when`(diceBox.dice2.isActive()).thenReturn(true)
        assertTrue(diceBox.getActiveDiceWithNumber(5) == null)
    }

    @Test
    fun given_getActiveDiceGreaterEqual_2_called_then_invoke_getActiveDiceWithNumber_2() {
        diceBox.getActiveDiceGreaterEqual(2)
        verify(diceBox).getActiveDiceWithNumber(2)
    }

    @Test
    fun given_getActiveDiceGreaterEqual_2_called_when_getActiveDiceWithNumber_2_is_null_and_getActiveDiceWithNumber_3_is_mockedDice_then_return_mockedDice() {
        val mockedDice = mock(Dice::class.java)
        `when`(diceBox.getActiveDiceWithNumber(2)).thenReturn(null)
        `when`(diceBox.getActiveDiceWithNumber(3)).thenReturn(mockedDice)
        val diceResult = diceBox.getActiveDiceGreaterEqual(2)
        assertTrue(diceResult == mockedDice)
    }

    @Test
    fun given_getActiveDiceGreaterEqual_2_called_when_getActiveDiceWithNumber_2_to_5_is_null_and_getActiveDiceWithNumber_6_is_mockedDice_then_return_mockedDice() {
        val mockedDice = mock(Dice::class.java)
        `when`(diceBox.getActiveDiceWithNumber(2)).thenReturn(null)
        `when`(diceBox.getActiveDiceWithNumber(3)).thenReturn(null)
        `when`(diceBox.getActiveDiceWithNumber(4)).thenReturn(null)
        `when`(diceBox.getActiveDiceWithNumber(5)).thenReturn(null)
        `when`(diceBox.getActiveDiceWithNumber(6)).thenReturn(mockedDice)
        val diceResult = diceBox.getActiveDiceGreaterEqual(2)
        assertTrue(diceResult == mockedDice)
    }

    @Test(expected = DiceRangeException::class)
    fun given_getActiveDiceGreaterEqual_8_called_then_throw_diceRangeException() {
        diceBox.getActiveDiceGreaterEqual(8)
    }

    @Test(expected = DiceException::class)
    fun given_useDice_called_when_dice_is_not_exist_in_diceBox_then_throw_diceException() {
        val dice = mock(Dice::class.java)
        diceBox.useDice(dice)
    }

    @Test(expected = DiceException::class)
    fun given_useDice_called_when_parameter_used_before_then_throw_diceException() {
        `when`(diceBox.dice2.used).thenReturn(true)
        diceBox.useDice(diceBox.dice2)
    }

    @Test
    fun given_useDice_called_when_parameter_is_dice1_in_then_dice1_used_should_be_called() {
        diceBox.useDice(diceBox.dice1)
        verify(diceBox.dice1).used = true
    }

    @Test
    fun given_useDice_called_when_parameter_is_dice2_in_then_dice2_used_should_be_called() {
        diceBox.useDice(diceBox.dice2)
        verify(diceBox.dice2).used = true
    }

    @Test
    fun given_isAtLeastOneDiceEnable_called_when_dice4_is_disable_and_dice1_to_dice3_is_enable_then_isAtLeastOneDiceEnable_should_return_true() {
        val enableDice  = mock(Dice::class.java)
        val disableDice = mock(Dice::class.java)
        `when`(enableDice.enabled).thenReturn(true)
        `when`(disableDice.enabled).thenReturn(false)
        `when`(diceBox.dice1).thenReturn(enableDice)
        `when`(diceBox.dice2).thenReturn(enableDice)
        `when`(diceBox.dice3).thenReturn(enableDice)
        `when`(diceBox.dice4).thenReturn(disableDice)
        assertTrue(diceBox.isAtLeastOneDiceEnable())
    }

    @Test
    fun given_isAtLeastOneDiceEnable_called_when_dice1_and_dice2_are_disable_then_isAtLeastOneDiceEnable_should_return_false() {
        val disableDice = mock(Dice::class.java)
        `when`(disableDice.enabled).thenReturn(false)
        `when`(diceBox.dice1).thenReturn(disableDice)
        `when`(diceBox.dice2).thenReturn(disableDice)
        assertFalse(diceBox.isAtLeastOneDiceEnable())
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

class SpyDiceBoxModuleTest : DiceBoxModule() {

    override fun provideDiceBox(dice1: Dice, dice2: Dice): DiceBox {
        return spy(super.provideDiceBox(dice1, dice2))
    }

    override fun provideDice(random: Random): Dice {
        return spy(super.provideDice(random))
    }

    override fun provideRandom(): Random {
        return spy(super.provideRandom())
    }
}