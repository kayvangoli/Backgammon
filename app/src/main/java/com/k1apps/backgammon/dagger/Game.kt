package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.Constants.Companion.NORMAL_PIECE_LIST
import com.k1apps.backgammon.Constants.Companion.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.Companion.REVERSE_PIECE_LIST
import com.k1apps.backgammon.Constants.Companion.REVERSE_PLAYER
import com.k1apps.backgammon.buisness.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope

@Scope
@Retention
annotation class GameScope

@GameScope
@Component(
    modules = [GameModule::class, DiceBoxModule::class, PieceListModule::class, PlayerModule::class]
)
interface GameComponent {
}

@Module(includes = [DiceBoxModule::class, PieceListModule::class, PlayerModule::class])
class GameModule {
    @Provides
    @GameScope
    fun provideReferee(
        board: Board,
        diceBox: DiceBox,
        @Named(NORMAL_PLAYER) player1: Player,
        @Named(REVERSE_PLAYER) player2: Player,
        diceDistributor: DiceDistributor
    ): Referee {
        return RefereeImpl(board, diceBox, player1, player2, diceDistributor)
    }

    @Provides
    @GameScope
    fun provideBoard(
        @Named(NORMAL_PIECE_LIST) normalPieceList: ArrayList<Piece>,
        @Named(REVERSE_PIECE_LIST) reversePieceList: ArrayList<Piece>
    ): Board {
        return BoardImpl(normalPieceList, reversePieceList)
    }

    @Provides
    @GameScope
    fun provideDiceDistributor(
        @Named(NORMAL_PLAYER) player1: Player,
        @Named(REVERSE_PLAYER) player2: Player,
        diceBox: DiceBox
    ): DiceDistributorImpl {
        return DiceDistributorImpl(player1, player2, diceBox)
    }

}

@Module
class PlayerModule {
    @GameScope
    @Provides
    @Named(NORMAL_PLAYER)
    fun providePlayer1(@Named(NORMAL_PIECE_LIST) pieceList: ArrayList<Piece>): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal)
    }

    @GameScope
    @Provides
    @Named(REVERSE_PLAYER)
    fun providePlayer2(@Named(REVERSE_PIECE_LIST) pieceList: ArrayList<Piece>): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Revers)
    }

}

@Module
class PieceListModule {

    @Provides
    @GameScope
    @Named(NORMAL_PIECE_LIST)
    fun reverseList(): ArrayList<Piece> {
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
    @Named(REVERSE_PIECE_LIST)
    fun normalList(): ArrayList<Piece> {
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