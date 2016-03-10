package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/28/15.
 */
public class Queen extends ChessPiece {

    /*
    Constructor for the queen piece.
    Sets the color, x, and y positions
     */
    public Queen(boolean white, int x, int y){
        super(white, x, y);
    }

    @Override
    public void generateMoves(ChessBoard curr) {
        ArrayList<Location> moves = new ArrayList<>();

        //Right movement goes until it hits the chess board boundary or hits a chess piece. If the chess piece's
        //color is not equal to this pieces color, it adds that to the possible move
        int i = 1;
        while(pos.x + i <= 7 && curr.board[pos.x + i][pos.y].pieceHold == null) {
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
        while(pos.x - i >= 0 && curr.board[pos.x - i][pos.y].pieceHold == null) {
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
        while(pos.y + i <= 7 && curr.board[pos.x][pos.y + i].pieceHold == null) {
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
        while(pos.y - i >= 0 && curr.board[pos.x][pos.y - i].pieceHold == null) {
            moves.add(new Location(pos.x, pos.y - i));
            i++;
        }
        if(pos.y - i >= 0 && curr.board[pos.x][pos.y - i].pieceHold.getColor() != this.getColor()) {
            moves.add(new Location(pos.x, pos.y - i));
            if(curr.board[pos.x][pos.y-i].pieceHold.getClass() == King.class){
                curr.checkPiece = curr.board[pos.x][pos.y-i].pieceHold;
            }
        }
        i = 1;

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
        Queen newPiece = new Queen(this.getColor(), this.getLocation().x, this.getLocation().y);
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
            return "Q";
        else
            return "q";
    }


}
