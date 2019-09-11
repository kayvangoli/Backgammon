package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
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