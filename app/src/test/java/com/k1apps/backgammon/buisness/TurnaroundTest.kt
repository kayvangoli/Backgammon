package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.DaggerGameComponent
import org.junit.Before
import javax.inject.Inject

class TurnaroundTest {
    @Inject
    lateinit var turnaround: Turnaround

    @Before
    fun setup() {
        DaggerGameComponentTest.create().inject(this)
    }

    fun when_player1_is_6_and_player2_is_2_then_player1_should_be_selected() {
        turnaround.onThrewDice()
    }
}