package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/8/15.
 */
public abstract class ChessPiece {

    public boolean white;
    public Location pos = new Location(0, 0);
    public boolean moved = false;
    public ArrayList<Location> posMoves = new ArrayList<>();
    public boolean removed = false;

    public ChessPiece(boolean white, int x, int y) {
        this.white = white;
        pos.x = x;
        pos.y = y;
        this.moved = false;
    }

    /*
    This interface is used so that each chess piece can be referenced under one name.
     */
    public void setLocation(int x, int y){
        pos.x = x;
        pos.y = y;
        moved = true;
    }

    public void setRemoved(boolean removed){
        this.removed = removed;
    }

    public void setMoved(boolean moved){
        this.moved = moved;
    }

    public void setPosMoves(ArrayList<Location> moves){
        this.posMoves = moves;
    }

    public boolean getRemoved(){
        return this.removed;
    }

    public Location getLocation(){
        return this.pos;
    }

    public boolean getColor(){
        return this.white;
    }

    public boolean getMoved(){
        return this.moved;
    }

    public ArrayList<Location> getMoves(){
        return this.posMoves;
    }

    public abstract void generateMoves(ChessBoard curr);

    public abstract ChessPiece copy();

    public abstract String toString();

}
