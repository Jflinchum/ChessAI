package com.company;

/**
 * Created by jonathanflinchum on 12/19/15.
 */
public class ChessSquare {

    public int x;
    public int y;

    ChessPiece pieceHold;

    /*
    Initially creates an empty square with the x and y positions.
     */
    public ChessSquare(int x, int y){
        this.x = x;
        this.y = y;
        pieceHold = null;
    }

    /*
    Prints out [] for an empty square
    Prints out the chess piece symbol if it isn't empty
     */
    public String toString(){
        if(pieceHold!=null){
            return pieceHold.toString();
        }
        else{
            return "[]";
        }
    }
}
