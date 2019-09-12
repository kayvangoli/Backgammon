package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.dagger.DaggerRefereeComponentTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named

class RefereeTest {
    @Inject
    lateinit var refereeImpl: RefereeImpl
    @Inject
    lateinit var board: Board
    @Inject
    @field:Named("normalPlayer")
    lateinit var player1: Player
    @Inject
    @field:Named("reversePlayer")
    lateinit var player2: Player
    @Inject
    lateinit var diceDistributor: DiceDistributor
    @Inject
    lateinit var diceBox: DiceBox


    @Before
    fun setup() {
        DaggerRefereeComponentTest.create().inject(this)
    }

    @Test
    fun when_referee_started_then_board_should_be_init_called() {
        refereeImpl.start()
        verify(board, times(1)).initBoard()
    }

    @Test
    fun when_referee_started_then_set_pieces_to_players() {
        refereeImpl.start()
        verify(player1, times(1)).pieceList = ArgumentMatchers.any()
        verify(player2, times(1)).pieceList = ArgumentMatchers.any()
    }

    @Test
    fun when_referee_started_then_set_dices_to_players() {
        refereeImpl.start()
        verify(player1, times(1)).dice = diceBox.dice1
        verify(player2, times(1)).dice = diceBox.dice2
    }

    @Test
    fun when_referee_started_then_start_dice_distributor() {
        refereeImpl.start()
        verify(diceDistributor, times(1)).start()
    }
}