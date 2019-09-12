package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import org.mockito.Mockito.mock

@GameScope
@Component(modules = [BoardModule::class])
interface BoardComponentTest {
    fun inject(boardTest: BoardTest)
}

@GameScope
@Component(modules = [TurnaroundModuleTest::class])
interface TurnaroundComponentTest {
    fun inject(turnaroundTest: TurnaroundTest)
}

@Module(includes = [DiceBoxModule::class])
class TurnaroundModuleTest {
    @Provides
    fun provideTurnaround(diceBox: DiceBox): TurnaroundImpl {
        return TurnaroundImpl(diceBox)
    }
}

@GameScope
@Component(modules = [PlayerModuleTest::class])
interface PlayerComponentTest{
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
    fun provideTurnaround(): TurnaroundImpl {
        val mockTurnaroundImpl = mock(TurnaroundImpl::class.java)
        EventBus.getDefault().register(mockTurnaroundImpl)
        return mockTurnaroundImpl
    }
}