package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/30/15.
 */
public class Pawn extends ChessPiece {

    public boolean enPassant = false;

    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Pawn(boolean white, int x, int y){
        super(white, x, y);
    }

    /*
    Sets the x and y positions
     */
    @Override
    public void setLocation(int x, int y) {
        if(Math.abs(y - pos.y) == 2){
            enPassant = true;
        }
        pos.x = x;
        pos.y = y;
        moved = true;
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

        /*enPassant is when a pawn can take another pawn that just moved two spaces forward by moving to the space it
        would have been at if it only moved one space forward. This situation only occurs if the enemy pawn moved 2 spaces
        forward and ends up right next to this pawn.
         */
        if(pos.x+1 <= 7){
            ChessPiece piece = curr.board[pos.x+1][pos.y].pieceHold;
            if (piece != null && piece.getClass() == Pawn.class && piece.getColor() != this.getColor()) {
                Pawn ePawn = (Pawn) piece;
                if(ePawn.enPassant){
                    moves.add(new Location(pos.x+1, pos.y+i));
                }
            }
        }
        if(pos.x-1 >= 0){
            ChessPiece piece = curr.board[pos.x-1][pos.y].pieceHold;
            if (piece != null && piece.getClass() == Pawn.class && piece.getColor() != this.getColor()) {
                Pawn ePawn = (Pawn) piece;
                if(ePawn.enPassant){
                    moves.add(new Location(pos.x-1, pos.y+i));
                }
            }
        }
        this.posMoves = moves;
    }

    public ChessPiece copy(){
        Pawn newPiece = new Pawn(this.getColor(), this.getLocation().x, this.getLocation().y);
        newPiece.pos.x = this.getLocation().x;
        newPiece.pos.y = this.getLocation().y;
        newPiece.moved = this.getMoved();
        newPiece.removed = this.getRemoved();
        newPiece.white = this.getColor();
        newPiece.posMoves.addAll(this.posMoves);
        newPiece.enPassant = this.enPassant;
        return newPiece;
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