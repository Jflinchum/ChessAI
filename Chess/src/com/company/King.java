package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/8/15.
 */
public class King extends ChessPiece {

    /*
    Constructor for the king piece.
    Sets the color, x, and y positions
     */
    public King(boolean white, int x, int y){
        super(white, x, y);
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

        //Generating castle moves
        if(!moved){
            Location rookLoc = new Location(0, pos.y);
            ChessPiece rook = curr.board[rookLoc.x][rookLoc.y].pieceHold;
            if(rook != null && rook.getClass() == Rook.class && rook.getColor() == white && checkCastle(curr, rookLoc)){
                moves.add(rookLoc);
            }
            rookLoc = new Location(7, pos.y);
            rook = curr.board[rookLoc.x][rookLoc.y].pieceHold;
            if(rook != null && rook.getClass() == Rook.class && rook.getColor() == white && checkCastle(curr, rookLoc)){
                moves.add(rookLoc);
            }
        }

        this.posMoves = moves;
    }

    /*
    A method that will check if it is possible to castle. Returns true if it can
     */
    private boolean checkCastle(ChessBoard curr, Location rookPos){
        //Which direction to check spaces for
        int direction = 1;
        if(pos.x > rookPos.x)
            direction = -1;
        //Checking the spaces in between the rook and king
        for(int i = pos.x+direction; i != rookPos.x; i+=direction){
            if(curr.board[i][pos.y].pieceHold != null){
                return false;
            }
        }

        //Creating the new location
        Location kingNew = new Location(pos.x+(2*direction), pos.y);

        //Making sure castleing doesn't put it in check.
        if(curr.board[pos.x][pos.y].pieceHold.getColor()){
            for(ChessPiece enemy : curr.blackPieces){
                for(Location move : enemy.getMoves()){
                    if(move.equals(kingNew)){
                        return false;
                    }
                }
            }
        }
        else{
            for(ChessPiece enemy : curr.whitePieces){
                for(Location move : enemy.getMoves()){
                    if(move.equals(kingNew)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ChessPiece copy(){
        King newPiece = new King(this.getColor(), this.getLocation().x, this.getLocation().y);
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
            return "K";
        else
            return "k";
    }
}
