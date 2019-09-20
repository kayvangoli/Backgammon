package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import dagger.Component
import dagger.Module
import dagger.Provides
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named

class DiceBoxTest {
    @Inject
    @field:Named("diceBoxMock")
    lateinit var diceBoxWithMockedDices: DiceBox
    @Inject
    lateinit var realDiceBox: DiceBox

    @Before
    fun setup() {
        DaggerDiceBoxComponentTest.create().inject(this)
    }

    @Test
    fun when_dice_box_roll_called_then_dice1_and_dice2_roll_must_be_called() {
        diceBoxWithMockedDices.roll()
        verify(diceBoxWithMockedDices.dice1, times(1)).roll()
        verify(diceBoxWithMockedDices.dice2, times(1)).roll()
    }

    @Test
    fun when_dice_box_roll_never_called_then_dice1_and_dice2_number_must_be_null_and_disable() {
        assertTrue(realDiceBox.dice1.number == null)
        assertTrue(realDiceBox.dice2.number == null)
        assertFalse(realDiceBox.dice1.enabled)
        assertFalse(realDiceBox.dice2.enabled)
    }

    @Test
    fun when_dice_roll_called_and_dices_number_is_pair_then_dice3_and_dice4_should_not_be_null() {
        `when`(diceBoxWithMockedDices.dice1.number).thenReturn(4)
        `when`(diceBoxWithMockedDices.dice2.number).thenReturn(4)
        diceBoxWithMockedDices.roll()
        assertTrue(diceBoxWithMockedDices.dice3 != null)
        assertTrue(diceBoxWithMockedDices.dice4 != null)
    }

    @Test
    fun when_dice_roll_called_and_dices_number_is_pair_then_dice3_and_dice4_number_should_be_equal_to_dice2_number() {
        `when`(diceBoxWithMockedDices.dice1.number).thenReturn(4)
        `when`(diceBoxWithMockedDices.dice2.number).thenReturn(4)
        diceBoxWithMockedDices.roll()
        assertTrue(diceBoxWithMockedDices.dice3!!.number!! == 4.toByte())
        assertTrue(diceBoxWithMockedDices.dice4!!.number!! == 4.toByte())
    }
}

@GameScope
@Component(modules = [DiceBoxModuleTest::class, DiceBoxModule::class])
interface DiceBoxComponentTest {
    fun inject(diceBoxTest: DiceBoxTest)
}

@Module
class DiceBoxModuleTest {
    @GameScope
    @Provides
    @Named("diceBoxMock")
    fun provideDiceBox(
        @Named("mockDice") dice1: Dice,
        @Named("mockDice") dice2: Dice
    ): DiceBox {
        return DiceBoxImpl(dice1, dice2)
    }

    @Provides
    @Named("mockDice")
    fun provideDice(): Dice {
        return mock(Dice::class.java)
    }
}