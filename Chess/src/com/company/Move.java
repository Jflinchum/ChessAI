package com.company;

/**
 * Created by jonathanflinchum on 2/1/16.
 */
public class Move {

    Location from;
    Location pos;

    /*
    A move object that contains a piece and a location to move.
     */
    public Move(Location from, Location pos){
        this.from = from;
        this.pos = pos;
    }

    public String toString(){
        return from.toString() + " : " + pos.toString();
    }

    public Move copy (){
        return new Move(this.from.copy(), this.pos.copy());
    }
}
