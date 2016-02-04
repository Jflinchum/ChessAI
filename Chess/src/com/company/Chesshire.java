package com.company;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jonathanflinchum on 2/2/16.
 */
public class Chesshire implements Player {

    private boolean white;

    public Chesshire(boolean white){
        this.white = white;
    }

    @Override
    public Move getMove(ChessBoard board) {
        ArrayList<Move> moves = new ArrayList<>();
        if(white){
            for(ChessPiece piece : board.whitePieces){
                if(!piece.getRemoved()) {
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece, move));
                    }
                }
            }
        }
        else{
            for(ChessPiece piece : board.blackPieces){
                if(!piece.getRemoved()) {
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece, move));
                    }
                }
            }
        }
        Random rand = new Random();
        return moves.get(rand.nextInt(moves.size()));
    }

    @Override
    public ChessPiece upgradePawn(ChessBoard board, ChessPiece pawn) {
        ChessPiece newPiece = new Queen(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
        newPiece.setMoved(true);
        board.removePiece(pawn);
        board.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
        board.blackPieces.add(newPiece);
        return newPiece;
    }
}
