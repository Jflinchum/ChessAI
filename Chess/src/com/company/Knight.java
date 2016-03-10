package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/30/15.
 */
public class Knight extends ChessPiece {

    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Knight(boolean white, int x, int y){
        super(white, x, y);
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

    public ChessPiece copy(){
        Knight newPiece = new Knight(this.getColor(), this.getLocation().x, this.getLocation().y);
        newPiece.pos.x = this.getLocation().x;
        newPiece.pos.y = this.getLocation().y;
        newPiece.moved = this.getMoved();
        newPiece.removed = this.getRemoved();
        newPiece.white = this.getColor();
        newPiece.posMoves.addAll(this.posMoves);
        return newPiece;
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