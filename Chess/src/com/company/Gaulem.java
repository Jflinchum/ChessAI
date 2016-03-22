package com.company;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by jonathanflinchum on 2/2/16.
 */
public class Gaulem implements Player {

    private boolean white;
    private static int kWeight = 200;
    private static int qWeight = 9;
    private static int rWeight = 5;
    private static int nWeight = 3;
    private static int bWeight = 3;
    private static int pWeight = 1;
    private static double mobWeight = 0.1;
    private static int maxDepth = 15;

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
                    { -.2, -.1,-.1,-.1,-.1,-.1, -.1,-.2}};

    public static double[][] pSquareTable =
            {{ 0,   0,  0,  0,  0,  0,   0,  0},
                    { .5,  .5, .5, .5, .5, .5,  .5, .5},
                    { .1,  .1, .2, .3, .3, .2,  .1, .1},
                    {.05, .05, .1, .5, .5, .1, .05,.05},
                    {  0,   0,  0, .2, .2,  0,   0,  0},
                    {.05,-.05,-.1,  0,  0,-.1,-.05,.05},
                    {.05,  .1, .1,-.2,-.2, .1,  .1,.05},
                    {  0,   0,  0,  0,  0,  0,   0,  0}};

    public Gaulem(boolean white) {
        this.white = white;
    }

    @Override
    public Move getMove(ChessBoard board) {
        long timeBefore = -System.currentTimeMillis();
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

        //Creating a new monitor class which keeps track of the best move
        Monitor m = new Monitor();
        ExecutorService pool = Executors.newCachedThreadPool();
        //Creates a new thread in the pool for each move
        for (Move move : moves) {
            pool.execute(new moveThread(move, board, m, white));
        }
        pool.shutdown();
        //Attempts to shut down the pool and waits 60 seconds
        try {
            pool.awaitTermination(60L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(m.moveChoice + " " + m.moveWeight + " " + (System.currentTimeMillis() + timeBefore));
        return m.moveChoice;
    }

    /*
    A monitor class which keeps track of the best move.
    Contains a synchronized function that each thread can call to check the move against the best move
     */
    private static class Monitor{
        Move moveChoice = null;
        double moveWeight = -100000;

        public synchronized void moveCheck(Move newMove, double newMoveWeight){
            if(newMoveWeight > moveWeight){
                moveChoice = newMove;
                moveWeight = newMoveWeight;
            }
        }
    }

    /*
    A runnable that calculates the move weight using a recursive function to look multiple moves ahead
     */
    private static class moveThread implements Runnable{
        private Move move;
        private ChessBoard board;
        private double moveWeight = 0;
        private Monitor monitor;
        private boolean white;

        public moveThread(Move move, ChessBoard board, Monitor monitor, boolean white){
            this.move = move;
            this.board = board;
            this.monitor = monitor;
            this.white = white;
        }

        public void run(){
            //Copies the current board
            ChessBoard copy = board.copy();
            ChessPiece movePiece = copy.board[move.from.x][move.from.y].pieceHold;
            //Checking if the move is a castle
            if(movePiece != null && movePiece.getClass() == King.class && !movePiece.getMoved() && move.pos.y == (movePiece.white ? 0 : 7) && (move.pos.x == 0 || move.pos.x == 7)){
                copy.castle(move.from, move.pos);
            }
            //Checking if the move is a regular move
            else{
                copy.movePiece(move.copy());
            }
            //Checking if it can upgrade a pawn from the move
            if(movePiece != null && move.pos.y == (movePiece.white ? 7 : 0) && movePiece.getClass() == Pawn.class){
                upgradePawn(copy, copy.board[move.pos.x][move.pos.y].pieceHold);
            }

            double moveEval = evaluateBoard(copy, copy.turn%2==1);
            copy.turn++;
            //Calling the recursive function to look ahead
            moveEval += evalFunction(copy, maxDepth-1);
            this.moveWeight = moveEval;
            monitor.moveCheck(move, moveWeight);
        }

        /*
        A recursive function to check multiple moves ahead from the board and evaluate it
         */
        private double evalFunction(ChessBoard board, int depth) {
            ArrayList<Move> moves = new ArrayList<>();
            float tempo = 0;
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
                ChessPiece movePiece = copy.board[move.from.x][move.from.y].pieceHold;
                if(movePiece != null && movePiece.getClass() == King.class && !movePiece.getMoved() && move.pos.y == (movePiece.white ? 0 : 7) && (move.pos.x == 0 || move.pos.x == 7)){
                    copy.castle(move.from, move.pos);
                }
                else {
                    copy.movePiece(move.copy());
                }
                if(movePiece != null && move.pos.y == (movePiece.white ? 7 : 0) && movePiece.getClass() == Pawn.class){
                    upgradePawn(copy, copy.board[move.pos.x][move.pos.y].pieceHold);
                }
                double moveEval = evaluateBoard(copy, board.turn%2==1);
                //Adding the number of good moves that can result from this current move being looked at
                if(moveEval > 0){
                    tempo+=mobWeight/2;
                }
                //Subtracting the number of bad moves that can result from this current move being looked at
                else{
                    tempo-=mobWeight/2;
                }
                copy.turn++;

                if(depth > 0)
                    moveEval += evalFunction(copy, --depth);
                if (moveEval > moveWeight) {
                    moveWeight = moveEval;
                }
            }
            return -(moveWeight+tempo);
        }

        /*
        Upgrades the pawn on the board to a queen
         */
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

        /*
        Evaluating the board to see if the whoToMove player is in a good position or bad one
         */
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

            /*
            Counting all of the white pieces
             */
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

            /*
            Counting all of the black pieces
             */
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
                        eval += rSquareTable[7-piece.getLocation().y][piece.getLocation().x];
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

    /*
    The upgrade pawn class for the AI that is called from the game manager.
     */
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

}
