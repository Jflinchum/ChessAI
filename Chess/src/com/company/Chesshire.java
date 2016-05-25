package com.company;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by jonathanflinchum on 2/2/16.
 */
public class Chesshire implements Player {

    private boolean white;

    public Chesshire(boolean white) {
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
                        moves.add(new Move(piece.getLocation(), move, piece));
                    }
                }
            }
        } else {
            for (ChessPiece piece : board.blackPieces) {
                piece.generateMoves(board);
                if (!piece.getRemoved()) {
                    for (Location move : piece.getMoves()) {
                        moves.add(new Move(piece.getLocation(), move, piece));
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
            pool.awaitTermination(10L, TimeUnit.DAYS);
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

            double moveEval = Evaluation.evaluateBoard(copy, copy.turn%2==1);
            copy.turn++;
            //Calling the recursive function to look ahead
            if(Evaluation.maxDepth > 0) {
                Move[] history = new Move[Evaluation.maxDepth+2];
                ChessBoard[] boardHistory = new ChessBoard[Evaluation.maxDepth+2];
                history[0] = this.move.copy();
                boardHistory[0] = copy.copy();
                moveEval += Evaluation.evalFunction(copy, 0, history, boardHistory, white);
            }
            this.moveWeight = moveEval;
            monitor.moveCheck(move, moveWeight);
            //System.out.println(move + " " + " " + moveWeight);
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
