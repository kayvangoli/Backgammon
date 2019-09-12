package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.mockito.Mockito.mock
import javax.inject.Named

@GameScope
@Component(modules = [BoardModule::class])
interface BoardComponentTest {
    fun inject(boardTest: BoardTest)
}

@GameScope
@Component(modules = [DiceDistributorModuleTest::class])
interface DiceDistributorComponentTest {
    fun inject(diceDistributor: DiceDistributorTest)
}

@Module(includes = [DiceBoxModule::class])
class DiceDistributorModuleTest {
    @GameScope
    @Provides
    @Named("realPlayer")
    fun provideDiceDistributor1(
        @Named("normalPlayer") player1: Player,
        @Named("reversePlayer") player2: Player,
        diceBox: DiceBox
    ): DiceDistributorImpl {
        return DiceDistributorImpl(player1, player2, diceBox)
    }

    @GameScope
    @Provides
    @Named("mockPlayer")
    fun provideDiceDistributor2(
        @Named("normalMockPlayer") player1: Player,
        @Named("reverseMockPlayer") player2: Player,
        diceBox: DiceBox
    ): DiceDistributorImpl {
        return DiceDistributorImpl(player1, player2, diceBox)
    }

    @GameScope
    @Provides
    @Named("normalPlayer")
    fun providePlayer1(): Player {
        return PlayerImpl()
    }

    @GameScope
    @Provides
    @Named("reversePlayer")
    fun providePlayer2(): Player {
        return PlayerImpl()
    }

    @GameScope
    @Provides
    @Named("normalMockPlayer")
    fun providePlayer3(): Player {
        return mock(Player::class.java)
    }

    @GameScope
    @Provides
    @Named("reverseMockPlayer")
    fun providePlayer4(): Player {
        return mock(Player::class.java)
    }

}

@GameScope
@Component(modules = [PlayerModuleTest::class])
interface PlayerComponentTest {
    fun inject(playerTest: PlayerTest)
}


@Module
class PlayerModuleTest {
    @GameScope
    @Provides
    fun providePlayer(): Player {
        return PlayerImpl()
    }

    @GameScope
    @Provides
    fun provideTurnaround(): DiceDistributorImpl {
        val mockTurnaroundImpl = mock(DiceDistributorImpl::class.java)
        EventBus.getDefault().register(mockTurnaroundImpl)
        return mockTurnaroundImpl
    }
}

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
        @Named("normalPlayer") player1: Player,
        @Named("reversePlayer") player2: Player,
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
    @Named("normalPlayer")
    fun providePlayer1(): Player {
        return mock(Player::class.java)
    }

    @GameScope
    @Provides
    @Named("reversePlayer")
    fun providePlayer2(): Player {
        return mock(Player::class.java)
    }

}