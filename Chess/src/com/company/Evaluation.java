package com.company;

import java.util.ArrayList;

/**
 * Created by Jonathan Flinchum on 3/26/2016.
 */
public class Evaluation {

    private static int kWeight = 200;
    private static int qWeight = 9;
    private static int rWeight = 5;
    private static int nWeight = 3;
    private static int bWeight = 3;
    private static int pWeight = 1;
    private static int wastedMoveWeight = 5;
    private static double mobWeight = 0.1;
    public static int maxDepth = 3;

    public static double[][] kSquareTable =
            {{ -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.3, -.4, -.4, -.5, -.5, -.4, -.4, -.3},
                    {  -.2, -.3, -.3, -.4, -.4, -.3, -.3, -.2},
                    {  -.1, -.2, -.2, -.2, -.2, -.2,-.05, -.1},
                    {   .2,  .2,   0,   0,   0,   0,  .1,  .2},
                    {   .2,  .3,  .1,   0,   0,  .1,  .3,  .2}};

    public static double[][] qSquareTable =
            {{ -.2, -.1, -.1, -.05, -.05, -.1, -.1,  -.2},
                    {  -.1,   0,   0,    0,    0,   0,   0,  -.1},
                    {  -.1,   0, .05,  .05,  .05, .05,   0,  -.1},
                    { -.05,   0, .05,  .05,  .05, .05,   0, -.05},
                    {    0,   0, .05,  .05,  .05, .05,   0, -.05},
                    {  -.1, .05, .05,  .05,  .05, .05,   0,  -.1},
                    {  -.1,   0, .05,    0,    0,   0,   0,  -.1},
                    {  -.2, -.1, -.1, -.05, -.05, -.1, -.1,  -.2}};

    public static double[][] rSquareTable =
            {{   0,  0,  0,   0,   0,  0,  0,    0},
                    {  .05, .1, .1,  .1,  .1, .1, .1,  .05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    { -.05,  0,  0,   0,   0,  0,  0, -.05},
                    {    0,  0,  0, .05, .05,  0,  0,    0}};

    public static double[][] nSquareTable =
            {{-.5, -.4, -.3, -.3, -.3, -.3, -.4, -.5},
                    { -.4, -.2,   0,   0,   0,   0, -.2, -.4},
                    { -.3,   0,  .1, .15, .15,  .1,   0, -.3},
                    { -.3, .05, .15,  .2,  .2, .15, .05, -.3},
                    { -.3,   0, .15,  .2,  .2, .15,   0, -.3},
                    { -.3, .05,  .1, .15, .15,  .1, .05, -.3},
                    { -.4, -.2,   0, .05, .05,   0, -.2, -.4},
                    { -.5, -.4, -.3, -.3, -.3, -.3, -.4, -.5}};

    public static double[][] bSquareTable =
            {{-.2, -.1,-.1,-.1,-.1,-.1, -.1,-.2},
                    { -.1,   0,  0,  0,  0,  0,   0,-.1},
                    { -.1,   0,.05, .1, .1,.05,   0,-.1},
                    { -.1, .05,.05, .1, .1,.05, .05,-.1},
                    { -.1,   0, .1, .1, .1, .1,   0,-.1},
                    { -.1,  .1, .1, .1, .1, .1,  .1, -.1},
                    { -.1, .05,  0,  0,  0,  0, .05,-.1},
                    { -.2, -.1,-.2,-.1,-.1,-.2, -.1,-.2}};

    public static double[][] pSquareTable =
            {{ 0,   0,  0,  0,  0,  0,   0,  0},
                    { .5,  .5, .5, .5, .5, .5,  .5, .5},
                    { .1,  .1, .2, .3, .3, .2,  .1, .1},
                    {.05, .05, .1, .5, .5, .1, .05,.05},
                    {  0,   0,  0, .2, .2,  0,   0,  0},
                    {.05,-.05,-.1,  0,  0,-.1,-.05,.05},
                    {.05,  .1, .1,-.3,-.3, .1,  .1,.05},
                    {  0,   0,  0,  0,  0,  0,   0,  0}};

    /*
    A recursive function to check multiple moves ahead from the board and evaluate it
    */
    public static double evalFunction(ChessBoard board, int depth, Move[] history, ChessBoard[] boardHistory, boolean white) {
        ArrayList<Move> moves = new ArrayList<>();
        //Getting all moves from the white or black pieces into an array list
        if (board.turn % 2 == 1) {
            for (ChessPiece piece : board.whitePieces) {
                if (!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move, piece));
                    }
                }
            }
        } else {
            for (ChessPiece piece : board.blackPieces) {
                if (!piece.getRemoved()) {
                    piece.generateMoves(board);
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move, piece));
                    }
                }
            }
        }
        ChessBoard copy;
        double moveWeight = -1000000;
        for (Move move : moves) {
            copy = board.copy();
            ChessPiece movePiece = copy.board[move.from.x][move.from.y].pieceHold;
            if(movePiece != null && movePiece.getClass() == King.class && !movePiece.getMoved() && move.pos.y == (movePiece.white ? 0 : 7) && (move.pos.x == 0 || move.pos.x == 7)){
                copy.castle(move.from, move.pos);
            }
            else {
                copy.movePiece(move.copy());
            }
            if(movePiece != null && move.pos.y == (movePiece.white ? 7 : 0) && movePiece.getClass() == Pawn.class){
                upgradePawn(copy, copy.board[move.pos.x][move.pos.y].pieceHold, white);
            }
            double moveEval = evaluateBoard(copy, copy.turn%2==1);
            boardHistory[depth+1] = copy;
            history[depth+1] = move;
            copy.turn++;
            if(depth-1 >= 0){
                //&& evaluateBoard(boardHistory[depth], history[depth].piece.getColor()) <= evaluateBoard(boardHistory[depth-2], history[depth-2].piece.getColor())){
                if(move.piece.equals(history[depth-1].piece) && move.from.equals(history[depth-1].pos)
                        && evaluateBoard(boardHistory[depth+1], !(boardHistory[depth+1].turn % 2 == 1)) <= evaluateBoard(boardHistory[depth-1],!(boardHistory[depth-1].turn % 2 == 1))) {
                    moveEval -= wastedMoveWeight;
                }
            }
            if(depth < maxDepth) {
                moveEval += evalFunction(copy, 1+depth, history, boardHistory, white);
            }
            if (moveEval > moveWeight) {
                moveWeight = moveEval;
            }
        }
        return -(moveWeight);
    }

    /*
    Upgrades the pawn on the board to a queen
     */
    public static ChessPiece upgradePawn(ChessBoard board, ChessPiece pawn, boolean white) {
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

    /*
    Evaluating the board to see if the whoToMove player is in a good position or bad one
     */
    public static double evaluateBoard(ChessBoard board, boolean whoToMove) {
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
        ArrayList<ChessPiece> wPawns = new ArrayList<>();
        ArrayList<ChessPiece> bPawns = new ArrayList<>();
        int wMoves = 0;
        int bMoves = 0;

        double squareTable = 0;
            /*
            Counting all of the white pieces
             */
        for (ChessPiece piece : board.whitePieces) {
            piece.generateMoves(board);
            if (!piece.getRemoved()) {
                if (piece.getClass() == King.class) {
                    wK++;
                    squareTable += kSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Queen.class) {
                    wQ++;
                    squareTable += qSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Rook.class) {
                    wR++;
                    squareTable += rSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Knight.class) {
                    wN++;
                    squareTable += nSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Bishop.class) {
                    wB++;
                    squareTable += bSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Pawn.class) {
                    wPawns.add(piece);
                    squareTable += pSquareTable[7-piece.getLocation().y][piece.getLocation().x];
                }
                if(piece.getClass() != Queen.class ) {
                    wMoves += piece.getMoves().size();
                }
            }
        }

            /*
            Counting all of the black pieces
             */
        for (ChessPiece piece : board.blackPieces) {
            piece.generateMoves(board);
            if (!piece.getRemoved()) {
                if (piece.getClass() == King.class) {
                    bK++;
                    squareTable -= kSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Queen.class) {
                    bQ++;
                    squareTable -= qSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Rook.class) {
                    bR++;
                    squareTable -= rSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Knight.class) {
                    bN++;
                    squareTable -= nSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Bishop.class) {
                    bB++;
                    squareTable -= bSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if (piece.getClass() == Pawn.class) {
                    bPawns.add(piece);
                    squareTable -= pSquareTable[piece.getLocation().y][piece.getLocation().x];
                }
                if(piece.getClass() != Queen.class ) {
                    bMoves += piece.getMoves().size();
                }
            }
        }

        double doubleWPawns = 0;
        double doubleBPawns = 0;
        double isolatedWPawns = 0;
        double isolatedBPawns = 0;
        //Going through pawns and evaluating those.
        for(ChessPiece pawn : wPawns){
            boolean isolated = true;
            for(ChessPiece pawn2 : wPawns){
                if(pawn != pawn2) {
                    if (pawn.getLocation().x == pawn2.getLocation().x) {
                        doubleWPawns++;
                    }
                    if (pawn.getLocation().x == pawn2.getLocation().x+1) {
                        isolated = false;
                    }
                    if (pawn.getLocation().x == pawn2.getLocation().x-1) {
                        isolated = false;
                    }
                }
            }
            if(isolated){
                isolatedWPawns++;
            }
        }
        for(ChessPiece pawn : bPawns){
            boolean isolated = true;
            for(ChessPiece pawn2 : bPawns){
                if(pawn != pawn2) {
                    if (pawn.getLocation().x == pawn2.getLocation().x) {
                        doubleBPawns++;
                    }
                    if (pawn.getLocation().x == pawn2.getLocation().x+1) {
                        isolated = false;
                    }
                    if (pawn.getLocation().x == pawn2.getLocation().x-1) {
                        isolated = false;
                    }
                }
            }
            if(isolated){
                isolatedBPawns++;
            }
        }

        //Material
        eval += (kWeight * (wK - bK)
                + qWeight * (wQ - bQ)
                + rWeight * (wR - bR)
                + nWeight * (wN - bN)
                + bWeight * (wB - bB)
                + pWeight * ((wPawns.size() - doubleWPawns*(1/4) - isolatedWPawns*(1/4)) - (bPawns.size() - doubleBPawns*(1/4) - isolatedBPawns*(1/4))));
        //Mobility
        eval += mobWeight * (wMoves - bMoves);
        eval += squareTable;

        return eval * (whoToMove ? 1 : -1);
    }

}
