package com.company;

/**
 * Created by jonathanflinchum on 2/1/16.
 */
public class Move {

    ChessPiece piece;
    Location pos;

    /*
    A move object that contains a piece and a location to move.
     */
    public Move(ChessPiece piece, Location pos){
        this.piece = piece;
        this.pos = pos;
    }

    public String toString(){
        return piece.toString() + ": " + pos.toString();
    }

}
