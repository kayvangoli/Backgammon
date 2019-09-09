package com.k1apps.backgammon.buisness

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DiceBoxTest {

    private lateinit var diceBox: DiceBox

    @Before
    fun create_instance() {
        diceBox = DiceBox.instance
    }

    @Test
    fun dice_roll_random_test() {
        for (item in 0..100) {
            val pair = diceBox.roll()
            println("pair is ${pair.first}:${pair.second}")
            assertTrue(pair.first < 7 && pair.second >= 1)
        }
    }
}
