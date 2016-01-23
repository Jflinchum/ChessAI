package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class GameManager {

    public static void main(String[] args) {
        ChessBoard testboard = new ChessBoard();
        testboard.NormSetUp();
        Scanner input = new Scanner(System.in);
        String answer = "";
        /*
        This is the command loop for the user.
        Commands are:
        move n,m j,k
        stop
        help
        castle n,m j,k
        moves n,m
         */
        int turn = 0;
        while(!answer.equals("stop")){
            testboard.checkPiece = null;
            for(ChessPiece piece : testboard.whitePieces){
                if(!piece.getRemoved())
                    piece.generateMoves(testboard);
            }
            for(ChessPiece piece : testboard.blackPieces){
                if(!piece.getRemoved())
                    piece.generateMoves(testboard);
            }
            if(testboard.checkPiece!=null){
                System.out.println((testboard.checkPiece.getColor() ? "White's " : "Black's ") + "King is in check!");
                //If its king piece is in check, then remove moves that don't uncheck it
                if(testboard.checkPiece.getColor()){
                    for(ChessPiece piece : testboard.whitePieces){
                        if(!piece.getRemoved()) {
                            for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext(); ) {
                                Location move = iterator.next();
                                if (!testboard.RemovesCheck(piece, move)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
                else{
                    for(ChessPiece piece : testboard.blackPieces){
                        if(!piece.getRemoved()) {
                            for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext(); ) {
                                Location move = iterator.next();
                                if (!testboard.RemovesCheck(piece, move)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
            }
            ++turn;
            ArrayList<Location> blackMoves;
            ArrayList<Location> whiteMoves;
            //WIN CONDITIONS
            //If it is black's turn
            if(turn%2==0){
                blackMoves = new ArrayList<>();
                for(ChessPiece piece : testboard.blackPieces){
                    if(!piece.getRemoved())
                        blackMoves.addAll(piece.getMoves());
                }
                //If black has no moves
                if(blackMoves.size() == 0){
                    if(testboard.checkPiece == null){
                        System.out.println("The match ends in a draw.");
                    }
                    else if(!testboard.checkPiece.getColor()){
                        System.out.println("Checkmate. White wins.");
                    }
                    System.out.println(testboard);
                    break;
                }
            }
            //If it is white's turn
            else{
                whiteMoves = new ArrayList<>();
                for(ChessPiece piece : testboard.whitePieces){
                    if(!piece.getRemoved())
                        whiteMoves.addAll(piece.getMoves());
                }
                //If white has no moves
                if(whiteMoves.size() == 0){
                    if(testboard.checkPiece == null){
                        System.out.println("The match ends in a draw.");
                    }
                    else if(testboard.checkPiece.getColor()){
                        System.out.println("Checkmate. Black wins.");
                    }
                    System.out.println(testboard);
                    break;
                }
                System.out.println(whiteMoves);
            }
            System.out.println((turn%2 == 0 ? "Black " : "White ") + "Turn: " + turn);
            System.out.println(testboard);
            boolean validCommand = false;
            while(!validCommand) {
                System.out.print("Command: ");
                answer = input.nextLine();
                if (answer.length() >= 4 && answer.substring(0, 4).equals("help")) {
                    System.out.println("Commands:\nmove n,m j,k\nprint\nstop\ncastle n,m j,k\nmoves n,m");
                }
                else if (answer.length() >= 5 && answer.substring(0, 5).equals("moves")) {
                    if(answer.length() >= 9 && isNumeric(answer.substring(6, 7)) && isNumeric(answer.substring(8, 9))){
                        Location pos = new Location(Integer.parseInt(answer.substring(6,7)), Integer.parseInt(answer.substring(8,9)));
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
                else if (answer.length() >= 4 && answer.substring(0, 4).equals("move")) {
                    //move is a proper command it is the proper size and if the numbers at the proper positions are actually numbers.
                    if (answer.length() >= 12 && isNumeric(answer.substring(5, 6)) && isNumeric(answer.substring(7, 8))
                            && isNumeric(answer.substring(9, 10)) &&



                            isNumeric(answer.substring(11, 12))) {
                        Location oldLoc = new Location(Integer.parseInt(answer.substring(5,6)), Integer.parseInt(answer.substring(7, 8)));
                        Location newLoc = new Location(Integer.parseInt(answer.substring(9,10)), Integer.parseInt(answer.substring(11,12)));
                        ChessPiece piece = testboard.board[oldLoc.x][oldLoc.y].pieceHold;

                        if (piece != null) {
                            //Checking if the pieces color matches the color that should be moving
                            if((turn%2==1) == piece.getColor()){
                                //Moving the piece if it can
                                if (movePiece(piece, testboard, new Location(newLoc.x, newLoc.y)))
                                    validCommand = true;
                                //If it can't move the piece
                                else
                                    System.err.println("Invalid Move.");
                            }
                            //If it isn't the right color
                            else{
                                System.err.println("That piece is "
                                        + (piece.getColor() ? "White. " : "Black. ")
                                        + "It is currently " + (turn%2==0 ? "Black's " : "White's ") + "turn.");
                            }
                        }
                        else
                            System.err.println("There is no piece there.");
                    }
                    else
                        System.err.println("Invalid use of move command:\nProper use: move n,m j,k");

                }
                else if(answer.length() >= 6 && answer.substring(0, 6).equals("castle")){
                    if (answer.length() >= 14 &&



                            isNumeric(answer.substring(7, 8)) &&



                            isNumeric(answer.substring(9, 10))
                            &&





                            isNumeric(answer.substring(11, 12)) &&




                            isNumeric(answer.substring(13, 14))) {
                        Location kingLoc = new Location(Integer.parseInt(answer.substring(7,8)), Integer.parseInt(answer.substring(9,10)));
                        Location rookLoc = new Location(Integer.parseInt(answer.substring(11,12)), Integer.parseInt(answer.substring(13,14)));
                        ChessPiece kingPiece = testboard.board[kingLoc.x][kingLoc.y].pieceHold;
                        ChessPiece rookPiece = testboard.board[rookLoc.x][rookLoc.y].pieceHold;

                        if(kingPiece != null){
                            if(kingPiece.getClass() == King.class){
                                //Checking if it is the right color
                                if((turn%2==1) == kingPiece.getColor()){
                                    //Checking if the king is in check
                                    if(testboard.checkPiece != null && testboard.checkPiece == kingPiece){
                                        System.err.println("The king is currently in check.");
                                    }
                                    else{
                                        if(rookPiece != null && rookPiece.getClass() == Rook.class
                                                && rookPiece.getColor() == kingPiece.getColor() && rookLoc.y == kingLoc.y){
                                            if(kingPiece.getMoved()){
                                                System.err.println("You can not castle if the king has moved.");
                                            }
                                            else{
                                                if(!castle(testboard, kingLoc, rookLoc)){
                                                    System.err.println("Invalid castle.");
                                                }
                                                else{
                                                    validCommand = true;
                                                }
                                            }
                                        }
                                        else{
                                            System.err.println("The other piece must be a rook of the same color " +
                                                    "in order to castle.");
                                        }
                                    }
                                }
                                //If it isn't the right color
                                else{
                                    System.err.println("That piece is "
                                            + (kingPiece.getColor() ? "White. " : "Black. ")
                                            + "It is currently " + (turn%2==0 ? "Black's " : "White's ") + "turn.");
                                }
                            }
                            else
                                System.err.println("Must castle with a king.");
                        }
                        else
                            System.err.println("There is no piece there.");
                    }
                    else
                        System.err.println("Invalid use of castle command:\nProper use: castle n,m j,k");
                }
                else if (answer.length() >= 4 && answer.substring(0,4).equals("stop")){
                    validCommand = true;
                }
                //Reprinting the board
                else if (answer.length() >= 5 && answer.substring(0,5).equals("print")){
                    System.out.println((turn%2 == 0 ? "Black " : "White ") + "Turn: " + turn);
                    System.out.println(testboard);
                }
                else{
                    System.err.println("Invalid Command. Use \"help\" to view the command list.");
                }
            }
            //If it is black's turn
            if(turn%2==0){
                for(ChessPiece piece : testboard.blackPieces){
                    if(!piece.getRemoved() && piece.getClass() == Pawn.class && piece.getLocation().y == 0){
                        System.out.println("What piece do you want your pawn to be?");
                        answer = input.nextLine();
                        while(true) {
                            if (answer.equals("Queen")) {
                                Queen newPiece = new Queen(false, piece.getLocation().x, 0);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.blackPieces.add(newPiece);
                                break;
                            } else if (answer.equals("Rook")) {
                                Rook newPiece = new Rook(false, piece.getLocation().x, 0);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.blackPieces.add(newPiece);
                                break;
                            } else if (answer.equals("Bishop")) {
                                Bishop newPiece = new Bishop(false, piece.getLocation().x, 0);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.blackPieces.add(newPiece);
                                break;
                            } else if (answer.equals("Knight")) {
                                Knight newPiece = new Knight(false, piece.getLocation().x, 0);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.blackPieces.add(newPiece);
                                break;
                            } else {
                                System.out.println("Choose Queen, Rook, Bishop, or Knight.");
                            }
                        }
                        break;
                    }
                }
            }
            //If it is white's turn
            else{
                for(ChessPiece piece: testboard.whitePieces){
                    if(!piece.getRemoved() && piece.getClass() == Pawn.class && piece.getLocation().y == 7){
                        System.out.println("What piece do you want your pawn to be?");
                        answer = input.nextLine();
                        while(true) {
                            if (answer.equals("Queen")) {
                                Queen newPiece = new Queen(true, piece.getLocation().x, 7);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.whitePieces.add(newPiece);
                                break;
                            } else if (answer.equals("Rook")) {
                                Rook newPiece = new Rook(true, piece.getLocation().x, 7);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.whitePieces.add(newPiece);
                                break;
                            } else if (answer.equals("Bishop")) {
                                Bishop newPiece = new Bishop(true, piece.getLocation().x, 7);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.whitePieces.add(newPiece);
                                break;
                            } else if (answer.equals("Knight")) {
                                Knight newPiece = new Knight(true, piece.getLocation().x, 7);
                                newPiece.setMoved(true);
                                removePiece(piece, testboard);
                                testboard.board[newPiece.getLocation().x][newPiece.getLocation().y].pieceHold = newPiece;
                                testboard.whitePieces.add(newPiece);
                                break;
                            } else {
                                System.out.println("Choose Queen, Rook, Bishop, or Knight.");
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /*
    isNumeric function returns true if the string is a number and false otherwise
     */
    private static boolean


    isNumeric(String s){
        try{
            Double.parseDouble(s);
        }
        catch(NumberFormatException n){
            return false;
        }
        return true;
    }

    /*
    Castle will castle two pieces if it doesn't have any pieces in between and returns true if it does
     */
    private static boolean castle(ChessBoard curr, Location kingPos, Location rookPos){
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

        //Creating the new locations
        Location kingNew = new Location(kingPos.x+(2*direction), kingPos.y);
        Location rookNew = new Location(kingPos.x+direction, kingPos.y);

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
        //Setting the pieces to the new locations
        curr.board[kingPos.x][kingPos.y].pieceHold.setLocation(kingNew.x, kingNew.y);
        curr.board[kingNew.x][kingNew.y].pieceHold = curr.board[kingPos.x][kingPos.y].pieceHold;
        curr.board[kingPos.x][kingPos.y].pieceHold = null;
        curr.board[rookPos.x][rookPos.y].pieceHold.setLocation(rookNew.x, rookNew.y);
        curr.board[rookNew.x][rookNew.y].pieceHold = curr.board[rookPos.x][rookPos.y].pieceHold;
        curr.board[rookPos.x][rookPos.y].pieceHold = null;

        return true;
    }

    /*
    MovePiece takes a piece, location, and a board
     */
    private static boolean movePiece(ChessPiece piece, ChessBoard curr, Location pos){
        for(Location move : piece.getMoves()){
            if(move.equals(pos)){
                curr.board[piece.getLocation().x][piece.getLocation().y].pieceHold = null;
                if(curr.board[pos.x][pos.y].pieceHold != null)
                    removePiece(curr.board[pos.x][pos.y].pieceHold, curr);
                curr.board[pos.x][pos.y].pieceHold = piece;
                piece.setLocation(pos.x, pos.y);
                return true;
            }
        }
        return false;
    }

    /*
    Remove piece from board
     */
    private static void removePiece(ChessPiece piece, ChessBoard curr){
        curr.board[piece.getLocation().x][piece.getLocation().y].pieceHold = null;
        piece.setRemoved(true);
    }
}
