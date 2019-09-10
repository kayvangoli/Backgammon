package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
@Retention
annotation class GameScope

@Component(modules = [GameModule::class])
interface GameComponent {
}

@Module(includes = [BoardModule::class, DiceBoxModule::class])
class GameModule {
    @Provides
    @GameScope
    fun provideReferee(
        board: Board,
        turnaround: Turnaround,
        diceBox: DiceBox,
        player1: Player,
        player2: Player
    ): Referee {
        return RefereeImpl(board, turnaround, diceBox, player1, player2)
    }

    @Provides
    @GameScope
    fun provideTurnaround(): Turnaround {
        return TurnaroundImpl()
    }

    @Provides
    fun providePlayer(): Player {
        return PlayerImpl()
    }
}

@Module
class BoardModule {

    @Provides
    @GameScope
    fun provideBoard(
        pieceList1: ArrayList<Piece>,
        pieceList2: ArrayList<Piece>
    ): Board {
        return BoardImpl(pieceList1, pieceList2)
    }

    @Provides
    @GameScope
    fun providePieceList1(): ArrayList<Piece> {
        val arrayList: ArrayList<Piece> = arrayListOf()
        for (item in 0 until 15) {
            arrayList.add(getPieceNormal())
        }
        return arrayList
    }

    private fun getPieceNormal(): Piece {
        return PieceImpl(MoveType.Normal)
    }

    @Provides
    @GameScope
    fun providePieceList2(): ArrayList<Piece> {
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