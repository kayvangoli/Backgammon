package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import com.nhaarman.mockitokotlin2.mock
import dagger.Component
import dagger.Module
import dagger.Provides

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
    fun provideTurnaround(diceBox: DiceBox): Turnaround {
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
    fun providePlayer(diceRollCallback: DiceRollCallback): Player {
        return PlayerImpl(diceRollCallback)
    }

    @GameScope
    @Provides
    fun provideDiceRollCallback(): DiceRollCallback {
        return mock()
    }
}