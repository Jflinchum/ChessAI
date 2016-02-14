package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/29/15.
 */
public class Rook implements ChessPiece {

    public boolean white;
    public Location pos = new Location(0, 0);
    public ArrayList<Location> posMoves = new ArrayList<>();
    public boolean moved = false;
    public boolean removed = false;

    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Rook(boolean white, int x, int y){
        this.white = white;
        pos.x = x;
        pos.y = y;
        this.removed = false;
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
    public ArrayList<Location> getMoves() {
        return this.posMoves;
    }

    @Override
    public void generateMoves(ChessBoard curr) {
        ArrayList<Location> moves = new ArrayList<>();

        //Right movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        int i = 1;
        while(pos.x + i <= 7) {
            if(curr.board[pos.x+i][pos.y].pieceHold != null)
                break;
            moves.add(new Location(pos.x + i, pos.y));
            i++;
        }
        if(pos.x + i <= 7 && curr.board[pos.x + i][pos.y].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x + i, pos.y));
            //Checking if it puts the king in check
            if(curr.board[pos.x+i][pos.y].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x+i][pos.y].pieceHold;
            }
        }
        i = 1;

        //Left movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.x - i >= 0) {
            if(curr.board[pos.x - i][pos.y].pieceHold != null)
                break;
            moves.add(new Location(pos.x - i, pos.y));
            i++;
        }
        if(pos.x - i >= 0 && curr.board[pos.x - i][pos.y].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x - i, pos.y));
            if(curr.board[pos.x-i][pos.y].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x-i][pos.y].pieceHold;
            }
        }
        i = 1;

        //Up movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y + i <= 7) {
            if(curr.board[pos.x][pos.y + i].pieceHold != null)
                break;
            moves.add(new Location(pos.x, pos.y + i));
            i++;
        }
        if(pos.y + i <= 7 && curr.board[pos.x][pos.y + i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x, pos.y + i));
            if(curr.board[pos.x][pos.y+i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x][pos.y+i].pieceHold;
            }
        }
        i = 1;

        //Down movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y - i >= 0) {
            if(curr.board[pos.x][pos.y - i].pieceHold != null){
                break;
            }
            moves.add(new Location(pos.x, pos.y - i));
            i++;
        }
        if(pos.y - i >= 0 && curr.board[pos.x][pos.y - i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x, pos.y - i));
            if(curr.board[pos.x][pos.y-i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x][pos.y-i].pieceHold;
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
            return "R";
        else
            return "r";
    }


}
