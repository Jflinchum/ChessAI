package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/8/15.
 */
public class King implements ChessPiece {

    public boolean white;
    public Location pos = new Location(0, 0);
    public boolean moved = false;
    public ArrayList<Location> posMoves = new ArrayList<>();
    public boolean removed = false;

    /*
    Constructor for the king piece.
    Sets the color, x, and y positions
     */
    public King(boolean white, int x, int y){
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
        this.moved = true;
    }

    @Override
    public void setMoved(boolean moved){
        this.moved = moved;
    }

    @Override
    public void setPosMoves(ArrayList<Location> moves){ this.posMoves = moves; }

    @Override
    public Location getLocation() {
        return this.pos;
    }

    @Override
    public boolean getColor(){
        return this.white;
    }

    /*
    The king can move orthogonally and diagonally 1 square, unless that square is next to a king.
     */
    @Override
    public ArrayList<Location> getMoves(){
        return this.posMoves;
    }

    @Override
    public void generateMoves(ChessBoard curr) {
        ArrayList<Location> moves = new ArrayList<>();
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++) {
                //If statement to make sure it doesn't add staying in the same location as a possible move
                if (i != 0 || j != 0) {
                    Location checkLocation = new Location(pos.x+i, pos.y+j);
                    if(pos.x+i <= 7 && pos.x+i >= 0 && pos.y+j <= 7 && pos.y+j >= 0
                            && curr.CheckSquare(this, checkLocation)){
                        ChessPiece oldPiece = curr.board[pos.x+i][pos.y+j].pieceHold;
                        //Saving the old check piece, because it gets messed up here
                        ChessPiece oldCheckPiece = curr.checkPiece;
                        boolean check = false;
                        curr.board[pos.x+i][pos.y+j].pieceHold = this;
                        for(ChessPiece piece : (this.getColor() ? curr.blackPieces : curr.whitePieces)){
                            piece.generateMoves(curr);
                            for(Location move : piece.getMoves()){
                                if(move.equals(checkLocation)){
                                    check = true;
                                    break;
                                }
                            }
                            if(check)
                                break;
                        }
                        if(!check)
                            moves.add(new Location(pos.x+i, pos.y+j));
                        //Restoring changes
                        curr.board[pos.x+i][pos.y+j].pieceHold = oldPiece;
                        curr.checkPiece = oldCheckPiece;
                    }
                }
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
            return "K";
        else
            return "k";
    }


}