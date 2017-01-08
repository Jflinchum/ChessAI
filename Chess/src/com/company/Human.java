package com.company;

import java.util.Scanner;

/**
 * Created by jonathanflinchum on 2/1/16.
 */
public class Human implements Player {

    private boolean white;

    public Human(boolean white){
        this.white = white;
    }

    @Override
    public Move getMove(ChessBoard testboard) {
        Scanner input = new Scanner(System.in);
        String answer;

        boolean validCommand = false;
        while(!validCommand) {
            System.out.print("Command: ");
            answer = input.nextLine();
            String[] answerParams = answer.split(" ");
            if (answerParams[0].equals("help")) {
                System.out.println("Commands:\nmove n,m j,k\nprint\nstop\ncastle n,m j,k\nmoves n,m");
            }
            else if (answerParams[0].equals("moves")) {
                // Check if the position variable exists (n,m)
                if (answerParams.length >= 2) {
                    String[] position = answerParams[1].split(",");
                    // Validate position variable
                    if(isNumeric(position[0]) && isNumeric(position[1])){
                        Location pos = new Location(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
                        if(testboard.board[pos.x][pos.y].pieceHold != null){
                            System.out.println(testboard.board[pos.x][pos.y].pieceHold.toString() + " " + testboard.board[pos.x][pos.y].pieceHold.getMoves());
                        }
                        else{
                            System.err.println("There is no piece there.");
                        }
                    }
                    else{
                        System.err.println("Invalid use of command:\nProper use: moves n,m");
                    }
                }
                else {
                    System.err.println("Invalid use of command:\nProper use: moves n,m");
                }
            }
            else if (answerParams[0].equals("move")) {
                if (answerParams.length >= 3) {
                    String[] origin = answerParams[1].split(",");
                    String[] target = answerParams[2].split(",");
                    //move is a proper command it is the proper size and if the numbers at the proper positions are actually numbers.
                    if (isNumeric(origin) && isNumeric(target)) {
                        Location oldLoc = new Location(Integer.parseInt(origin[0]), Integer.parseInt(origin[1]));
                        Location newLoc = new Location(Integer.parseInt(target[0]), Integer.parseInt(target[1]));
                        ChessPiece piece = testboard.board[oldLoc.x][oldLoc.y].pieceHold;

                        if (piece != null) {
                            //Checking if the pieces color matches the color that should be moving
                            if ((testboard.turn % 2 == 1) == piece.getColor()) {
                                //Moving the piece if it can
                                //if (movePiece(piece, testboard, new Location(newLoc.x, newLoc.y)))
                                if (checkMove(piece, new Location(newLoc.x, newLoc.y)))
                                    return new Move(oldLoc, newLoc, piece);
                                    //If it can't move the piece
                                else
                                    System.err.println("Invalid Move.");
                            }
                            //If it isn't the right color
                            else {
                                System.err.println("That piece is "
                                        + (piece.getColor() ? "White. " : "Black. ")
                                        + "It is currently " + (testboard.turn % 2 == 0 ? "Black's " : "White's ") + "turn.");
                            }
                        } else
                            System.err.println("There is no piece there.");
                    } else
                        System.err.println("Invalid use of move command:\nProper use: move n,m j,k");
                }
                else {
                    System.err.println("Invalid use of move command:\nProper use: move n,m j,k");
                }
            }
            else if(answerParams[0].equals("castle")){
                if (answerParams.length >= 3) {
                    String[] origin = answerParams[1].split(",");
                    String[] target = answerParams[2].split(",");
                    if (isNumeric(origin) && isNumeric(target)) {
                        Location kingLoc = new Location(Integer.parseInt(origin[0]), Integer.parseInt(origin[1]));
                        Location rookLoc = new Location(Integer.parseInt(target[0]), Integer.parseInt(target[1]));
                        ChessPiece kingPiece = testboard.board[kingLoc.x][kingLoc.y].pieceHold;
                        ChessPiece rookPiece = testboard.board[rookLoc.x][rookLoc.y].pieceHold;

                        if (kingPiece != null) {
                            if (kingPiece.getClass() == King.class) {
                                //Checking if it is the right color
                                if ((testboard.turn % 2 == 1) == kingPiece.getColor()) {
                                    //Checking if the king is in check
                                    if (testboard.checkPiece != null && testboard.checkPiece == kingPiece) {
                                        System.err.println("The king is currently in check.");
                                    } else {
                                        if (rookPiece != null && rookPiece.getClass() == Rook.class
                                                && rookPiece.getColor() == kingPiece.getColor() && rookLoc.y == kingLoc.y) {
                                            if (kingPiece.getMoved()) {
                                                System.err.println("You can not castle if the king has moved.");
                                            } else {
                                                if (!checkCastle(testboard, kingLoc, rookLoc)) {
                                                    System.err.println("Invalid castle.");
                                                } else {
                                                    return new Move(kingLoc, rookLoc, kingPiece);
                                                }
                                            }
                                        } else {
                                            System.err.println("The other piece must be a rook of the same color " +
                                                    "in order to castle.");
                                        }
                                    }
                                }
                                //If it isn't the right color
                                else {
                                    System.err.println("That piece is "
                                            + (kingPiece.getColor() ? "White. " : "Black. ")
                                            + "It is currently " + (testboard.turn % 2 == 0 ? "Black's " : "White's ") + "turn.");
                                }
                            } else
                                System.err.println("Must castle with a king.");
                        } else
                            System.err.println("There is no piece there.");
                    } else
                        System.err.println("Invalid use of castle command:\nProper use: castle n,m j,k");
                }
                else {
                    System.err.println("Invalid use of castle command:\nProper use: castle n,m j,k");
                }
            }
            else if (answerParams[0].equals("stop")){
                validCommand = true;
            }
            //Reprinting the board
            else if (answerParams[0].equals("print")){
                System.out.println((testboard.turn%2 == 0 ? "Black " : "White ") + "Turn: " + testboard.turn);
                System.out.println(testboard);
            }
            else{
                System.err.println("Invalid Command. Use \"help\" to view the command list.");
            }
        }
        return null;
    }

    /*
   isNumeric function returns true if the string is a number and false otherwise
    */
    private boolean isNumeric(String s){
        try{
            Double.parseDouble(s);
        }
        catch(NumberFormatException n){
            return false;
        }
        return true;
    }

    /*
   isNumeric function returns true if the array of strings are numbers and false otherwise
    */
    private boolean isNumeric(String[] s) {
        try {
            for(String sub : s) {
                Double.parseDouble(sub);
            }
        }
        catch(NumberFormatException n) {
            return false;
        }
        return true;
    }

    /*
    Checks if a move is valid with the selected piece
     */
    private boolean checkMove(ChessPiece piece, Location pos){
        for(Location move : piece.getMoves()) {
            if (move.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    /*
    A method that will check if it is possible to castle. Returns true if it can
     */
    private boolean checkCastle(ChessBoard curr, Location kingPos, Location rookPos){
        //Which direction to check spaces for
        int direction = 1;
        if(kingPos.x > rookPos.x)
            direction = -1;
        //Checking the spaces in between the rook and king
        for(int i = kingPos.x+direction; i != rookPos.x; i+=direction){
            if(curr.board[i][kingPos.y].pieceHold != null){
                return false;
            }
        }

        //Creating the new location
        Location kingNew = new Location(kingPos.x+(2*direction), kingPos.y);

        //Making sure castleing doesn't put it in check.
        if(curr.board[kingPos.x][kingPos.y].pieceHold.getColor()){
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

    public ChessPiece upgradePawn(ChessBoard curr, ChessPiece pawn){
        Scanner input = new Scanner(System.in);
        String answer;
        ChessPiece newPiece;
        System.out.println("What piece do you want your pawn to be?");
        answer = input.nextLine();
        while(true) {
            if (answer.equals("Queen")) {
                newPiece = new Queen(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
                newPiece.setMoved(true);
                curr.removePiece(pawn);
                curr.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                if(pawn.getColor()){
                    curr.whitePieces.add(newPiece);
                }
                else{
                    curr.blackPieces.add(newPiece);
                }
                break;
            } else if (answer.equals("Rook")) {
                newPiece = new Rook(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
                newPiece.setMoved(true);
                curr.removePiece(pawn);
                curr.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                if(pawn.getColor()){
                    curr.whitePieces.add(newPiece);
                }
                else{
                    curr.blackPieces.add(newPiece);
                }
                break;
            } else if (answer.equals("Bishop")) {
                newPiece = new Bishop(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
                newPiece.setMoved(true);
                curr.removePiece(pawn);
                curr.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                if(pawn.getColor()){
                    curr.whitePieces.add(newPiece);
                }
                else{
                    curr.blackPieces.add(newPiece);
                }
                break;
            } else if (answer.equals("Knight")) {
                newPiece = new Knight(pawn.getColor(), pawn.getLocation().x, pawn.getLocation().y);
                newPiece.setMoved(true);
                curr.removePiece(pawn);
                curr.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                if(pawn.getColor()){
                    curr.whitePieces.add(newPiece);
                }
                else{
                    curr.blackPieces.add(newPiece);
                }
                break;
            } else {
                System.out.println("Choose Queen, Rook, Bishop, or Knight.");
            }
        }
        return newPiece;
    }
}