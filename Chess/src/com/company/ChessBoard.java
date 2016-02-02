package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/8/15.
 */
public class ChessBoard {

    public ChessSquare[][] board =  new ChessSquare[8][8];
    public ArrayList<ChessPiece> whitePieces = new ArrayList<>();
    public ArrayList<ChessPiece> blackPieces = new ArrayList<>();
    //This is the piece that is in check
    public ChessPiece checkPiece = null;
    public int turn = 0;

    /*
    Constructing the chessboard, which is an 8x8 2D-array of chess squares.
     */
    public ChessBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new ChessSquare(i, j);
            }
        }
    }

    /*
    The normal chess board setup with white on the bottom and black at the top.
     */
    public void NormSetUp(){
        whitePieces.add(board[4][0].pieceHold = new King(true, 4, 0));
        whitePieces.add(board[3][0].pieceHold = new Queen(true, 3, 0));
        whitePieces.add(board[2][0].pieceHold = new Bishop(true, 2, 0));
        whitePieces.add(board[5][0].pieceHold = new Bishop(true, 5, 0));
        whitePieces.add(board[1][0].pieceHold = new Knight(true, 1, 0));
        whitePieces.add(board[6][0].pieceHold = new Knight(true, 6, 0));
        whitePieces.add(board[0][0].pieceHold = new Rook(true, 0, 0));
        whitePieces.add(board[7][0].pieceHold = new Rook(true, 7, 0));

        blackPieces.add(board[3][7].pieceHold = new King(false, 3, 7));
        blackPieces.add(board[4][7].pieceHold = new Queen(false, 4, 7));
        blackPieces.add(board[2][7].pieceHold = new Bishop(false, 2, 7));
        blackPieces.add(board[5][7].pieceHold = new Bishop(false, 5, 7));
        blackPieces.add(board[1][7].pieceHold = new Knight(false, 1, 7));
        blackPieces.add(board[6][7].pieceHold = new Knight(false, 6, 7));
        blackPieces.add(board[0][7].pieceHold = new Rook(false, 0, 7));
        blackPieces.add(board[7][7].pieceHold = new Rook(false, 7, 7));
        for(int i = 0; i < 8; i++) {
            whitePieces.add(board[i][1].pieceHold = new Pawn(true, i, 1));
            blackPieces.add(board[i][6].pieceHold = new Pawn(false, i, 6));
        }
        /*
        Must generate all moves after set up
         */
        for(ChessPiece piece : whitePieces){
            piece.generateMoves(this);
        }
        for(ChessPiece piece : blackPieces){
            piece.generateMoves(this);
        }
    }

    /*
    Checks if the square is okay to move to. If the square holds the enemy piece, or if it does not hold a piece,
    then it is okay. Also puts the king in check if the square holds a king of a different color
     */
    public boolean CheckSquare(ChessPiece piece, Location pos){
        if(board[pos.x][pos.y].pieceHold!=null && board[pos.x][pos.y].pieceHold.getColor() != piece.getColor()
                && board[pos.x][pos.y].pieceHold.getClass() == King.class) {
            checkPiece = board[pos.x][pos.y].pieceHold;
        }
        return board[pos.x][pos.y].pieceHold == null || (board[pos.x][pos.y].pieceHold.getColor() != piece.getColor());
    }

    /*
    A deep copy function that copies everything from the given board into this one.
     */
    public ChessBoard copy(ChessBoard copyFrom){
        for(ChessPiece piece : copyFrom.whitePieces){
            if(piece.getClass() == King.class) {
                this.whitePieces.add(new King(true, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Queen.class){
                this.whitePieces.add(new Queen(true, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Knight.class){
                this.whitePieces.add(new Knight(true, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Bishop.class){
                this.whitePieces.add(new Bishop(true, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Rook.class){
                this.whitePieces.add(new Rook(true, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Pawn.class){
                this.whitePieces.add(new Pawn(true, 0, 0).copy(piece));
            }
            if(!piece.getRemoved())
                this.board[piece.getLocation().x][piece.getLocation().y].pieceHold = piece;
        }
        for(ChessPiece piece : copyFrom.blackPieces){
            if(piece.getClass() == King.class) {
                this.blackPieces.add(new King(false, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Queen.class){
                this.blackPieces.add(new Queen(false, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Knight.class){
                this.blackPieces.add(new Knight(false, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Bishop.class){
                this.blackPieces.add(new Bishop(false, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Rook.class){
                this.blackPieces.add(new Rook(false, 0, 0).copy(piece));
            }
            else if(piece.getClass() == Pawn.class){
                this.blackPieces.add(new Pawn(false, 0, 0).copy(piece));
            }
            if(!piece.getRemoved())
                this.board[piece.getLocation().x][piece.getLocation().y].pieceHold = piece;
        }
        return this;
    }

    /*
    Method to print out each chess square in the chess board
    It prints 7-i to flip the board around so that white is on bottom
    Uses string format to add some whitespace padding
     */
    public String toString(){
        String printBoard = "";
        for(int i = 0; i < 8; i++){
            printBoard += (7-i + ":");
            for(int j = 0; j < 8; j++){
                printBoard += String.format("%-3s", board[j][7-i].toString());
            }
            printBoard += "\n";
        }
        printBoard += "  0  1  2  3  4  5  6  7";
        return printBoard;
    }

    /*
    Method to check if a certain move removes the check piece. It performs the move and generates moves for each
    of the enemy pieces. If the move removes the check piece, returns true. Returns the board to it's original state
    after the check.
     */
    public boolean removesCheck(ChessPiece piece, Location pos){
        boolean check = true;
        //Saving the parts of the board that will change
        ChessPiece oldPiece = board[pos.x][pos.y].pieceHold;
        Location oldLoc = new Location(piece.getLocation().x, piece.getLocation().y);
        boolean moved = piece.getMoved();
        ChessPiece oldCheckPiece = checkPiece;

        //Changing the board
        board[pos.x][pos.y].pieceHold = piece;
        board[oldLoc.x][oldLoc.y].pieceHold = null;
        checkPiece = null;
        piece.setLocation(pos.x, pos.y);
        if(oldPiece != null)
            oldPiece.setRemoved(true);
        if(piece.getColor()){
            for(ChessPiece enemy : blackPieces){
                if(!enemy.getRemoved() && enemy.getClass() != King.class){
                    enemy.generateMoves(this);
                }
            }
        }
        else{
            for(ChessPiece enemy : whitePieces){
                if(!enemy.getRemoved() && enemy.getClass() != King.class){
                    enemy.generateMoves(this);
                }
            }
        }
        if(checkPiece == null || checkPiece.getColor() != piece.getColor())
            check = false;

        //Returning the board to its original state
        board[pos.x][pos.y].pieceHold = oldPiece;
        board[oldLoc.x][oldLoc.y].pieceHold = piece;
        piece.setLocation(oldLoc.x, oldLoc.y);
        if(oldPiece != null)
            oldPiece.setRemoved(false);
        piece.setMoved(moved);
        checkPiece = oldCheckPiece;

        return !check;
    }

    /*
    Remove piece from board
     */
    public void removePiece(ChessPiece piece){
        board[piece.getLocation().x][piece.getLocation().y].pieceHold = null;
        piece.setRemoved(true);
    }
}
