package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

@GameScope
@Component(modules = [BoardModule::class])
interface BoardComponentTest{
    fun inject(boardTest: BoardTest)
}

@GameScope
@Component(modules = [GameModule::class])
interface GameComponentTest{
}
