package com.k1apps.backgammon.dagger

import com.k1apps.backgammon.Constants.NORMAL_PIECE_LIST
import com.k1apps.backgammon.Constants.NORMAL_PLAYER
import com.k1apps.backgammon.Constants.REVERSE_PIECE_LIST
import com.k1apps.backgammon.Constants.REVERSE_PLAYER
import com.k1apps.backgammon.gamelogic.*
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Scope
import kotlin.random.Random

@Scope
@Retention
annotation class GameScope

@GameScope
@Component(modules = [GameModule::class])
interface GameComponent {
}

@Module(includes = [BoardModule::class, DiceDistributorModule::class])
class GameModule {
    @Provides
    @GameScope
    fun provideReferee(
        board: Board,
        diceDistributor: DiceDistributor
    ): Referee {
        return RefereeImpl(board, diceDistributor)
    }
}

@Module(includes = [PlayerModule::class, DiceBoxModule::class])
open class DiceDistributorModule {

    @GameScope
    @Provides
    open fun provideDiceDistributor(
        @Named(NORMAL_PLAYER) player1: Player,
        @Named(REVERSE_PLAYER) player2: Player,
        diceBox: DiceBox
    ): DiceDistributor {
        return DiceDistributorImpl(player1, player2, diceBox)
    }
}

@Module
open class BoardModule {
    @Provides
    @GameScope
    open fun provideBoard(
        @Named(NORMAL_PIECE_LIST) normalPieceList: ArrayList<Piece>,
        @Named(REVERSE_PIECE_LIST) reversePieceList: ArrayList<Piece>
    ): Board {
        return BoardImpl(normalPieceList, reversePieceList)
    }

}

@Module(includes = [PieceListModule::class, BoardModule::class])
open class PlayerModule {
    @GameScope
    @Provides
    @Named(NORMAL_PLAYER)
    open fun providePlayer1(
        @Named(NORMAL_PIECE_LIST) pieceList: ArrayList<Piece>,
        board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Normal, board)
    }

    @GameScope
    @Provides
    @Named(REVERSE_PLAYER)
    open fun providePlayer2(
        @Named(REVERSE_PIECE_LIST) pieceList: ArrayList<Piece>,
        board: Board
    ): Player {
        return PlayerImpl(PlayerType.LocalPlayer, pieceList, MoveType.Revers, board)
    }

}


@Module
open class PieceListModule {

    @Provides
    @GameScope
    @Named(NORMAL_PIECE_LIST)
    open fun provideReverseList(): ArrayList<Piece> {
        val arrayList: ArrayList<Piece> = arrayListOf()
        for (item in 0 until 15) {
            arrayList.add(PieceFactory.createNormalPiece())
        }
        pieceListArrangementNormal(arrayList)
        return arrayList
    }

    @Provides
    @GameScope
    @Named(REVERSE_PIECE_LIST)
    open fun provideNormalList(): ArrayList<Piece> {
        val arrayList: ArrayList<Piece> = arrayListOf()
        for (item in 0 until 15) {
            arrayList.add(PieceFactory.createReversePiece())
        }
        pieceListArrangementReverse(arrayList)
        return arrayList
    }

}

@Module
open class DiceBoxModule {
    @Provides
    @GameScope
    open fun provideDiceBox(
        dice1: Dice,
        dice2: Dice
    ): DiceBox {
        return DiceBoxImpl(dice1, dice2)
    }

    @Provides
    open fun provideDice(random: Random): Dice {
        return DiceImpl(random)
    }

    @Provides
    @GameScope
    open fun provideRandom(): Random {
        return Random
    }
}