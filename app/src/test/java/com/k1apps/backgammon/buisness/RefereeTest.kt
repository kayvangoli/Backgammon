package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants
import com.k1apps.backgammon.dagger.DiceBoxModule
import com.k1apps.backgammon.dagger.GameScope
import dagger.Component
import dagger.Module
import dagger.Provides
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named


@GameScope
@Component(modules = [RefereeModuleTest::class])
interface RefereeComponentTest {
    fun inject(refereeTest: RefereeTest)
}

@Module(includes = [DiceBoxModule::class])
class RefereeModuleTest {

    @GameScope
    @Provides
    fun provideReferee(
        board: Board,
        diceBox: DiceBox,
        @Named(Constants.NORMAL_PLAYER) player1: Player,
        @Named(Constants.REVERSE_PLAYER) player2: Player,
        diceDistributor: DiceDistributor
    ): RefereeImpl {
        return RefereeImpl(board, diceBox, player1, player2, diceDistributor)
    }

    @GameScope
    @Provides
    fun provideBoard(): Board {
        return mock(Board::class.java)
    }

    @GameScope
    @Provides
    fun provideDiceDistributor(): DiceDistributor {
        return mock(DiceDistributor::class.java)
    }

    @GameScope
    @Provides
    @Named(Constants.NORMAL_PLAYER)
    fun providePlayer1(): Player {
        return mock(Player::class.java)
    }

    @GameScope
    @Provides
    @Named(Constants.REVERSE_PLAYER)
    fun providePlayer2(): Player {
        return mock(Player::class.java)
    }

}

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

    @Test
    fun when_roll_called_then_pass_roll_to_player_if_turn_is_right() {
//        Mockito.`when`(diceDistributor.whichPlayerHasDice())
//        refereeImpl.roll(PlayerType.LocalPlayer)
//        verify()
    }
}