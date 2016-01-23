package com.company;

import java.util.ArrayList;

/**
 * Created by jonathanflinchum on 12/8/15.
 */
public interface ChessPiece {

    /*
    This interface is used so that each chess piece can be referenced under one name.
     */
    public void setLocation(int x, int y);
    public void setRemoved(boolean removed);
    public void setMoved(boolean moved);
    public void setPosMoves(ArrayList<Location> moves);

    public boolean getRemoved();
    public Location getLocation();
    public boolean getColor();
    public boolean getMoved();

    public ArrayList<Location> getMoves();
    public void generateMoves(ChessBoard curr);

    public ChessPiece copy(ChessPiece pieceFrom);
    String toString();

}
