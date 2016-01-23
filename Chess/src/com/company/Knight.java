package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/30/15.
 */
public class Knight implements ChessPiece {

    public boolean white;
    public Location pos = new Location(0, 0);
    public ArrayList<Location> posMoves = new ArrayList<>();
    public boolean removed = false;
    public boolean moved = false;


    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Knight(boolean white, int x, int y){
        this.white = white;
        pos.x = x;
        pos.y = y;
        this.moved = false;
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

        for(int y = -2; y <= 2; y += 4){
            for(int x =- 1; x <= 1; x += 2){
                if(pos.x+x <= 7 && pos.x+x >=0 && pos.y+y <= 7 && pos.y+y >= 0 && curr.CheckSquare(this, new Location(pos.x+x, pos.y+y)))
                    moves.add(new Location(pos.x+x, pos.y+y));
            }
        }
        for(int x = -2; x <= 2; x += 4){
            for(int y = -1; y <= 1; y += 2){
                if(pos.x+x <= 7 && pos.x+x >=0 && pos.y+y <= 7 && pos.y+y >= 0 && curr.CheckSquare(this, new Location(pos.x+x, pos.y+y)))
                    moves.add(new Location(pos.x+x, pos.y+y));
            }
        }

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
            return "N";
        else
            return "n";
    }


}