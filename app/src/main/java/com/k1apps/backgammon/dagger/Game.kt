package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention
annotation class GameScope

@GameScope
@Component(modules = [GameModule::class, BoardModule::class, DiceBoxModule::class])
interface GameComponent {
}

@Module
class GameModule {
    @Provides
    @GameScope
    fun provideReferee(
        board: Board,
        diceBox: DiceBox,
        player1: Player,
        player2: Player
    ): Referee {
        return RefereeImpl(board, diceBox, player1, player2)
    }

    @Provides
    @GameScope
    fun provideTurnaround(): Turnaround {
        return TurnaroundImpl()
    }

    @Provides
    fun providePlayer(diceRollCallback: DiceRollCallback): Player {
        return PlayerImpl(diceRollCallback)
    }

    @Provides
    fun provideDiceRollCallback(turnaround: Turnaround): DiceRollCallback {
        return turnaround
    }
}

@Module
class BoardModule {

    @Provides
    @GameScope
    fun provideBoard(): Board {
        return BoardImpl(getList1(), getList2())
    }

    private fun getList1(): ArrayList<Piece> {
        val arrayList: ArrayList<Piece> = arrayListOf()
        for (item in 0 until 15) {
            arrayList.add(getPieceNormal())
        }
        return arrayList
    }

    private fun getPieceNormal(): Piece {
        return PieceImpl(MoveType.Normal)
    }

    private fun getList2(): ArrayList<Piece> {
        val arrayList: ArrayList<Piece> = arrayListOf()
        for (item in 0 until 15) {
            arrayList.add(getPieceRevers())
        }
        return arrayList
    }

    private fun getPieceRevers(): Piece {
        return PieceImpl(MoveType.Revers)
    }
}

@Module
class DiceBoxModule {
    @Provides
    @GameScope
    fun provideDiceBox(): DiceBox {
        return DiceBoxImpl(getDice(), getDice())
    }

    private fun getDice(): Dice {
        return DiceImpl()
    }
}