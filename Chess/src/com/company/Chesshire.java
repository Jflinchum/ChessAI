package com.company;

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
    private int depth = 10;

    public double[][] kSquareTable =
                    {{ -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.2, -.3, -.3, -.4, -.4, -.3, -.3, -.2},
                    {  -.1, -.2, -.2, -.2, -.2, -.2,-.05, -.1},
                    {   .2,  .2,   0,   0,   0,   0,  .1,  .2},
                    {   .2,  .3,  .1,   0,   0,  .1,  .3,  .2}};

    public double[][] qSquareTable =
                    {{ -.2, -.1, -.1, -.05, -.05, -.1, -.1,  -.2},
                    {  -.1,   0,   0,    0,    0,   0,   0,  -.1},
                    {  -.1,   0, .05,  .05,  .05, .05,   0,  -.1},
                    { -.05,   0, .05,  .05,  .05, .05,   0, -.05},
                    {    0,   0, .05,  .05,  .05, .05,   0, -.05},
                    {  -.1, .05, .05,  .05,  .05, .05,   0,  -.1},
                    {  -.1,   0, .05,    0,    0,   0,   0,  -.1},
                    {  -.2, -.1, -.1, -.05, -.05, -.1, -.1,  -.2}};

    public double[][] rSquareTable =
                    {{   0,  0,  0,   0,   0,  0,  0,    0},
                    {  .05, .1, .1,  .1,  .1, .1, .1,  .05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    {    0,  0,  0, .05, .05,  0,  0,    0}};

    public double[][] nSquareTable =
                    {{-.5, -.4, -.3, -.3, -.3, -.3, -.4, -.5},
                    { -.4, -.2,   0,   0,   0,   0, -.2, -.4},
                    { -.3,   0,  .1, .15, .15,  .1,   0, -.3},
                    { -.3, .05, .15,  .2,  .2, .15, .05, -.3},
                    { -.3,   0, .15,  .2,  .2, .15,   0, -.3},
                    { -.3, .05,  .1, .15, .15,  .1, .05, -.3},
                    { -.4, -.2,   0, .05, .05,   0, -.2, -.4},
                    { -.5, -.4, -.3, -.3, -.3, -.3, -.4, -.5}};

    public double[][] bSquareTable =
                    {{-.2, -.1,-.1,-.1,-.1,-.1, -.1,-.2},
                    { -.1,   0,  0,  0,  0,  0,   0,-.1},
                    { -.1,   0,.05, .1, .1,.05,   0,-.1},
                    { -.1, .05,.05, .1, .1,.05, .05,-.1},
                    { -.1,   0, .1, .1, .1, .1,   0,-.1},
                    { -.1,  .1, .1, .1, .1, .1,  .1, -.1},
                    { -.1, .05,  0,  0,  0,  0, .05,-.1},
                    { -.2, -.1,-.1,-.1,-.1,-.1, -.1,-.2}};

    public double[][] pSquareTable =
                    {{ 0,   0,  0,  0,  0,  0,   0,  0},
                    { .5,  .5, .5, .5, .5, .5,  .5, .5},
                    { .1,  .1, .2, .3, .3, .2,  .1, .1},
                    {.05, .05, .1,.25,.25, .1, .05,.05},
                    {  0,   0,  0, .2, .2,  0,   0,  0},
                    {.05,-.05,-.1,  0,  0,-.1,-.05,.05},
                    {.05,  .1, .1,-.2,-.2, .1,  .1,.05},
                    {  0,   0,  0,  0,  0,  0,   0,  0}};

    public Chesshire(boolean white) {
        this.white = white;
    }

    @Override
    public Move getMove(ChessBoard board) {
        ArrayList<Move> moves = new ArrayList<>();
        //Getting all moves from the white or black pieces into an array list
        if (white) {
            for (ChessPiece piece : board.whitePieces) {
                if (!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        } else {
            for (ChessPiece piece : board.blackPieces) {
                piece.generateMoves(board);
                if (!piece.getRemoved()) {
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }

        ChessBoard copy;
        Move moveChoice = null;
        double moveWeight = -100000;
        for (Move move : moves) {
            copy = board.copy();
            copy.movePiece(move.copy());
            double moveEval = evaluateBoard(copy, this.white);
            copy.turn++;
            moveEval += evalFunction(copy, --depth);
            if (moveEval > moveWeight) {
                moveChoice = move;
                moveWeight = moveEval;
            }
        }

        System.out.println(moveChoice + " " + moveWeight);
        return moveChoice;
    }

    private double evalFunction(ChessBoard board, int depth) {
        ArrayList<Move> moves = new ArrayList<>();
        //Getting all moves from the white or black pieces into an array list
        if (board.turn % 2 == 1) {
            for (ChessPiece piece : board.whitePieces) {
                if (!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        } else {
            for (ChessPiece piece : board.blackPieces) {
                if (!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move));
                    }
                }
            }
        }
        ChessBoard copy;
        double moveWeight = -1000000;
        for (Move move : moves) {
            copy = board.copy();
            copy.movePiece(move.copy());
            double moveEval = evaluateBoard(copy, copy.turn%2==1);
            copy.turn++;
            if(depth > 0)
                moveEval += evalFunction(copy, --depth);
            if (moveEval > moveWeight) {
                moveWeight = moveEval;
            }
        }
        return moveWeight * ( this.white == (board.turn%2==1) ? 1 : -1);
    }

    @Override
    public ChessPiece upgradePawn(ChessBoard board, ChessPiece pawn) {
        ChessPiece newPiece = new Queen(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
        newPiece.setMoved(true);
        board.removePiece(pawn);
        board.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
        if (white) {
            board.whitePieces.add(newPiece);
        } else {
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

        for (ChessPiece piece : board.whitePieces) {
            piece.generateMoves(board);
            if (!piece.getRemoved()) {
                if (piece.getClass() == King.class) {
                    wK++;
                    eval += kSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Queen.class) {
                    wQ++;
                    eval += qSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Rook.class) {
                    wR++;
                    eval += rSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Knight.class) {
                    wN++;
                    eval += nSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Bishop.class) {
                    wB++;
                    eval += bSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Pawn.class) {
                    wP++;
                    eval += pSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if(piece.getClass() != Queen.class ) {
                    wMoves += piece.getMoves().size();
                }
            }
        }

        for (ChessPiece piece : board.blackPieces) {
            piece.generateMoves(board);
            if (!piece.getRemoved()) {
                if (piece.getClass() == King.class) {
                    bK++;
                    eval += kSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Queen.class) {
                    bQ++;
                    eval += qSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Rook.class) {
                    bR++;
                    eval += bSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Knight.class) {
                    bN++;
                    eval += nSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Bishop.class) {
                    bB++;
                    eval += bSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Pawn.class) {
                    bP++;
                    eval += pSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if(piece.getClass() != Queen.class ) {
                    bMoves += piece.getMoves().size();
                }
            }
        }

        //Material
        eval += (kWeight * (wK - bK)
                + qWeight * (wQ - bQ)
                + rWeight * (wR - bR)
                + nWeight * (wN - bN)
                + bWeight * (wB - bB)
                + pWeight * (wP - bP));
        //Mobility
        eval += mobWeight * (wMoves - bMoves);

        return eval * (whoToMove ? 1 : -1);
    }
}
