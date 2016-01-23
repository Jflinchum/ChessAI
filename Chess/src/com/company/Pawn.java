package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/30/15.
 */
public class Pawn implements ChessPiece {

    public boolean white;
    public Location pos = new Location(0, 0);
    public boolean moved = false;
    public boolean removed = false;
    public ArrayList<Location> posMoves = new ArrayList<>();

    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Pawn(boolean white, int x, int y){
        this.white = white;
        pos.x = x;
        pos.y = y;
        this.moved = false;
        this.removed = false;
    }

    /*
    Sets the x and y positions
     */
    @Override
    public void setLocation(int x, int y) {
        pos.x = x;
        pos.y = y;
        moved = true;
    }

    @Override
    public void setMoved(boolean moved){
        this.moved = moved;
    }

    @Override
    public void setPosMoves(ArrayList<Location> moves){ this.posMoves = moves; }

    @Override
    public Location getLocation() {
        return pos;
    }

    @Override
    public boolean getColor(){
        return this.white;
    }

    @Override
    public ArrayList<Location> getMoves(){
        return this.posMoves;
    }

    @Override
    public void generateMoves(ChessBoard curr) {
        ArrayList<Location> moves = new ArrayList<>();

        int i = this.white ? 1 : -1;
        //Moves up if it is white and down if it is black. Can't move to a square with a piece on it.
        if(pos.y+i <= 7 && pos.y+i >= 0 && curr.board[pos.x][pos.y+i].pieceHold == null)
            moves.add(new Location(pos.x, pos.y+i));

        //Can move diagonally one space in the direction it is going if there is a piece of the opposite color there.
        if(pos.y+i <= 7 && pos.y+i >= 0 && pos.x+1 <= 7 && curr.board[pos.x+1][pos.y+i].pieceHold != null
                && curr.board[pos.x+1][pos.y+i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x + 1, pos.y + i));
            if(curr.board[pos.x+1][pos.y+i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x+1][pos.y+i].pieceHold;
            }
        }
        if(pos.y+i <= 7 && pos.y+i >= 0 && pos.x-1 >= 0 && curr.board[pos.x-1][pos.y+i].pieceHold != null
                && curr.board[pos.x-1][pos.y+i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x - 1, pos.y + i));
            if(curr.board[pos.x-1][pos.y+i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x-1][pos.y+i].pieceHold;
            }
        }

        //Can move two spaces forward if it hasn't moved yet and there is nothing in front of it.
        if(!moved && pos.y+(i*2) <= 7 && pos.y+(i*2) >= 0 && curr.board[pos.x][pos.y+i].pieceHold == null
                && curr.board[pos.x][pos.y+(i*2)].pieceHold == null)
            moves.add(new Location(pos.x, pos.y+(i*2)));

        this.posMoves = moves;
    }

    @Override
    public void setRemoved(boolean removed){ this.removed = removed; }

    @Override
    public boolean getRemoved(){ return this.removed; }

    @Override
    public boolean getMoved(){ return this.moved; }

    @Override
    public ChessPiece copy(ChessPiece pieceFrom){
        this.pos.x = pieceFrom.getLocation().x;
        this.pos.y = pieceFrom.getLocation().y;
        this.moved = pieceFrom.getMoved();
        this.removed = pieceFrom.getRemoved();
        this.white = pieceFrom.getColor();
        this.posMoves.addAll(pieceFrom.getMoves());
        return this;
    }

    /*
    Prints out the symbol of the chess piece. Upper case is white, lower case is black
     */
    public String toString(){
        if(white)
            return "P";
        else
            return "p";
    }
}