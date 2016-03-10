package com.company;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;


/**
 * Created by jonathanflinchum on 2/2/16.
 */
public class Chesshire implements Player {

    private boolean white;
    private int kWeight = 200;
    private int qWeight = 9;
    private int rWeight = 5;
    private int nWeight = 3;
    private int bWeight = 3;
    private int pWeight = 1;
    private double mobWeight = 0.1;
    private int depth = 5;

    public Chesshire(boolean white){
        this.white = white;
    }

    @Override
    public Move getMove(ChessBoard board) {
        ArrayList<Move> moves = new ArrayList<>();
        //Getting all moves from the white or black pieces into an array list
        if(white){
            for(ChessPiece piece : board.whitePieces){
                if(!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }
        else{
            for(ChessPiece piece : board.blackPieces){
                piece.generateMoves(board);
                if(!piece.getRemoved()) {
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }

        ChessBoard copy = board.copy();
        Move moveChoice = moves.get(0);
        copy.movePiece(moveChoice.copy());
        double moveWeight = evaluateBoard(copy, this.white);
        for(Move move : moves){
            copy = board.copy();
            copy.movePiece(move.copy());
            double moveEval = evalFunction(copy, depth);
            if(moveEval > moveWeight){
                moveChoice = move;
                moveWeight = moveEval;
            }
        }

        System.out.println(moveChoice + " " + moveWeight);
        return moveChoice;
    }

    @Override
    public ChessPiece upgradePawn(ChessBoard board, ChessPiece pawn) {
        ChessPiece newPiece = new Queen(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
        newPiece.setMoved(true);
        board.removePiece(pawn);
        board.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
        if(white){
            board.whitePieces.add(newPiece);
        }
        else{
            board.blackPieces.add(newPiece);
        }
        return newPiece;
    }

    public double evaluateBoard(ChessBoard board, boolean whoToMove) {
        double eval = 0;
        int wK = 0;
        int bK = 0;
        int wQ = 0;
        int bQ = 0;
        int wR = 0;
        int bR = 0;
        int wN = 0;
        int bN = 0;
        int wB = 0;
        int bB = 0;
        int wP = 0;
        int bP = 0;
        int wMoves = 0;
        int bMoves = 0;

        for (ChessPiece piece : board.whitePieces){
            piece.generateMoves(board);
            if(!piece.getRemoved()){
                if(piece.getClass() == King.class)
                    wK++;
                if(piece.getClass() == Queen.class)
                    wQ++;
                if(piece.getClass() == Rook.class)
                    wR++;
                if(piece.getClass() == Knight.class)
                    wN++;
                if(piece.getClass() == Bishop.class)
                    wB++;
                if(piece.getClass() == Pawn.class)
                    wP++;
                for(int i = 0; i < piece.getMoves().size(); i++) {
                    wMoves++;
                }
            }
        }

        for (ChessPiece piece : board.blackPieces){
            piece.generateMoves(board);
            if(!piece.getRemoved()){
                if(piece.getClass() == King.class)
                    bK++;
                if(piece.getClass() == Queen.class)
                    bQ++;
                if(piece.getClass() == Rook.class)
                    bR++;
                if(piece.getClass() == Knight.class)
                    bN++;
                if(piece.getClass() == Bishop.class)
                    bB++;
                if(piece.getClass() == Pawn.class)
                    bP++;
                for(int i = 0; i < piece.getMoves().size(); i++) {
                    bMoves++;
                }
            }
        }

        //Material
        eval += (kWeight*(wK-bK)
                + qWeight*(wQ-bQ)
                + rWeight*(wR-bR)
                + nWeight*(wN-bN)
                + bWeight*(wB-bB)
                + pWeight*(wP-bP));
        //Mobility
        eval += mobWeight * (wMoves-bMoves);

        return eval * (whoToMove ? 1 : -1);
    }

    private double evalFunction(ChessBoard board, int depth){
        ArrayList<Move> moves = new ArrayList<>();
        //Getting all moves from the white or black pieces into an array list
        if(board.turn%2==1){
            for(ChessPiece piece : board.whitePieces){
                if(!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }
        else{
            for(ChessPiece piece : board.blackPieces){
                if(!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }

        ChessBoard copy = board.copy();
        Move moveChoice = moves.get(0);
        copy.movePiece(moveChoice.copy());
        double totalEval = evaluateBoard(copy, board.turn%2==1);
        for(Move move : moves){
            copy = board.copy();
            copy.movePiece(move.copy());
            double moveEval = evaluateBoard(copy, board.turn%2==1);
            copy.turn++;
            if(depth <= 0){
                moveEval += evalFunction(copy, depth--);
            }
            if(moveEval > totalEval){
                totalEval = moveEval;
            }
        }
        return totalEval;
    }
}
