package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/29/15.
 */
public class Bishop extends ChessPiece {

    /*
    Constructor for the bishop piece.
    Sets the color, x, and y positions
     */
    public Bishop(boolean white, int x, int y){
        super(white, x, y);
    }

    @Override
    public void generateMoves(ChessBoard curr) {
        ArrayList<Location> moves = new ArrayList<>();
        int i = 1;

        //The diagonal move checks
        //Up Right movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y + i <= 7 && pos.x + i <= 7 && curr.board[pos.x + i][pos.y + i].pieceHold == null) {
            moves.add(new Location(pos.x + i, pos.y + i));
            i++;
        }
        if(pos.y + i <= 7 && pos.x + i <= 7 && curr.board[pos.x + i][pos.y + i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x + i, pos.y + i));
            if(curr.board[pos.x+i][pos.y+i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x+i][pos.y+i].pieceHold;
            }
        }
        i = 1;

        //Down Right movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y - i >= 0 && pos.x + i <= 7 && curr.board[pos.x + i][pos.y - i].pieceHold == null) {
            moves.add(new Location(pos.x + i, pos.y - i));
            i++;
        }
        if(pos.y - i >= 0 && pos.x + i <= 7 && curr.board[pos.x + i][pos.y - i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x + i, pos.y - i));
            if(curr.board[pos.x+i][pos.y-i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x+i][pos.y-i].pieceHold;
            }
        }
        i = 1;

        //Up Left movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y + i <= 7 && pos.x - i >= 0 && curr.board[pos.x - i][pos.y + i].pieceHold == null) {
            moves.add(new Location(pos.x - i, pos.y + i));
            i++;
        }
        if(pos.y + i <= 7 && pos.x - i >= 0 && curr.board[pos.x - i][pos.y + i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x - i, pos.y + i));
            if(curr.board[pos.x-i][pos.y+i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x-i][pos.y+i].pieceHold;
            }
        }
        i = 1;

        //Down Left movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        while(pos.y - i >= 0 && pos.x - i >= 0 && curr.board[pos.x - i][pos.y - i].pieceHold == null) {
            moves.add(new Location(pos.x - i, pos.y - i));
            i++;
        }
        if(pos.y - i >= 0 && pos.x - i >= 0 && curr.board[pos.x - i][pos.y - i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x - i, pos.y - i));
            if(curr.board[pos.x-i][pos.y-i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x-i][pos.y-i].pieceHold;
            }
        }

        this.posMoves = moves;
    }

    public ChessPiece copy(){
        Bishop newPiece = new Bishop(this.getColor(), this.getLocation().x, this.getLocation().y);
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
            return "B";
        else
            return "b";
    }


}
