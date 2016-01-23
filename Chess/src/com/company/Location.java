package com.company;

/**
 * Created by jonathanflinchum on 12/27/15.
 */
public class Location {

    int x;
    int y;

    public Location(int x, int y){
        this.x = x;
        this.y = y;
    }

    public String toString(){
        return ("{X: " + x + " Y: " + y + "}");
    }

    public boolean equals(Location compare){
        if(compare.x == this.x && compare.y == this.y)
            return true;
        else
            return false;
    }
}
