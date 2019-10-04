package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.Constants.NORMAL_PIECE_LIST
import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.REVERSE_PIECE_LIST
import com.k1apps.backgammon.Constants.REVERSE_PLAYER
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
    modules = [GameModule::class, DiceBoxModule::class, PieceListModule::class,
        PlayerModule::class, BoardModule::class]
)
interface GameComponent {
}

@Module(
    includes = [DiceBoxModule::class, PieceListModule::class, PlayerModule::class,
        BoardModule::class]
)
class GameModule {
    @Provides
    @GameScope
    fun provideReferee(
        board: Board,
        diceDistributor: DiceDistributor
    ): Referee {
        return RefereeImpl(board, diceDistributor)
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
class BoardModule {
    @Provides
    @GameScope
    fun provideBoard(
        @Named(NORMAL_PIECE_LIST) normalPieceList: ArrayList<Piece>,
        @Named(REVERSE_PIECE_LIST) reversePieceList: ArrayList<Piece>
    ): Board {
        return BoardImpl(normalPieceList, reversePieceList)
    }

}

@Module
class PlayerModule {
    @GameScope
    @Provides
    @Named(NORMAL_PLAYER)
    fun providePlayer1(
        @Named(NORMAL_PIECE_LIST) pieceList: ArrayList<Piece>,
        board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal, board)
    }

    @GameScope
    @Provides
    @Named(REVERSE_PLAYER)
    fun providePlayer2(
        @Named(REVERSE_PIECE_LIST) pieceList: ArrayList<Piece>,
        board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Revers, board)
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
open class DiceBoxModule {
    @Provides
    @GameScope
    fun provideDiceBox(
        dice1: Dice,
        dice2: Dice
    ): DiceBox {
        return DiceBoxImpl(dice1, dice2)
    }

    @Provides
    open fun provideDice(): Dice {
        return DiceImpl()
    }
}