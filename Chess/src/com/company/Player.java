package com.company;

/**
 * Created by jonathanflinchum on 2/1/16.
 */
public interface Player {

    public Move getMove(ChessBoard board);
    public ChessPiece upgradePawn(ChessBoard board, ChessPiece pawn);

}
