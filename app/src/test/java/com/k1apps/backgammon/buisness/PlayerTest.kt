package com.k1apps.backgammon.buisness

import com.k1apps.backgammon.Constants.Companion.NORMAL_PIECE_LIST
import com.k1apps.backgammon.buisness.event.DiceThrownEvent
import com.k1apps.backgammon.dagger.GameScope
import com.k1apps.backgammon.dagger.PieceListModule
import dagger.Component
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

class PlayerTest {
    @Inject
    lateinit var player: Player
    @Inject
    lateinit var diceDistributorImpl: DiceDistributorImpl

    @Before
    fun setup() {
        DaggerPlayerComponentTest.create().inject(this)
    }

    @Test
    fun when_roll_called_with_dice_then_roll_dice_callback_invoked() {
        val diceMock: Dice = mock(Dice::class.java)
        Mockito.`when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        player.roll()
        verify(diceDistributorImpl, times(1)).onEvent(DiceThrownEvent(player, 2))
    }

    @Test
    fun when_retakeDice_called_then_dice_should_be_null() {
        val diceMock: Dice = mock(Dice::class.java)
        Mockito.`when`(diceMock.roll()).thenReturn(2)
        player.dice = diceMock
        assertTrue(player.dice != null)
        player.retakeDice()
        assertTrue(player.dice == null)
    }
}


@GameScope
@Component(modules = [PlayerModuleTest::class, PieceListModule::class])
interface PlayerComponentTest {
    fun inject(playerTest: PlayerTest)
}


@Module(includes = [PieceListModule::class])
class PlayerModuleTest {
    @GameScope
    @Provides
    fun providePlayer(
        @Named(NORMAL_PIECE_LIST) pieceList: ArrayList<Piece>): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal)
    }

    @GameScope
    @Provides
    fun provideDiceDistributor(): DiceDistributorImpl {
        val mockTurnaroundImpl = mock(DiceDistributorImpl::class.java)
        EventBus.getDefault().register(mockTurnaroundImpl)
        return mockTurnaroundImpl
    }
}
