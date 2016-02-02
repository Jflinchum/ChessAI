package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.lang.ClassLoader;

public class GameManager {

    static Player white;
    static Player black;

    public static void main(String[] args) {
        ChessBoard testboard = new ChessBoard();
        testboard.NormSetUp();
        Scanner input = new Scanner(System.in);
        String answer = "";

        while(!answer.equals("Human") || !answer.equals("Computer")) {
            System.out.println("Who is white? Human or Computer?");
            answer = input.nextLine();
            if (answer.equals("Human")) {
                white = new Human();
                break;
            }
            else if(answer.equals("Computer")){

                break;
            }
        }
        answer = "";
        while(!answer.equals("Human") || !answer.equals("Computer")) {
            System.out.println("Who is black? Human or Computer?");
            answer = input.nextLine();
            if (answer.equals("Human")) {
                black = new Human();
                break;
            }
            else if(answer.equals("Computer")){

                break;
            }
        }
        /*
        This is the command loop for the user.
        Commands are:
        move n,m j,k
        stop
        help
        castle n,m j,k
        moves n,m
         */
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
                            for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext();) {
                                Location move = iterator.next();
                                if (!testboard.removesCheck(piece, move)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
                else{
                    for(ChessPiece piece : testboard.blackPieces){
                        if(!piece.getRemoved()) {
                            for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext();) {
                                Location move = iterator.next();
                                if (!testboard.removesCheck(piece, move)) {
                                    iterator.remove();
                                }
                            }
                        }
                    }
                }
            }
            enPassant(testboard);
            ++testboard.turn;
            ArrayList<Location> blackMoves;
            ArrayList<Location> whiteMoves;

            //If it is black's turn
            if(testboard.turn%2==0){
                //WIN CONDITIONS
                blackMoves = new ArrayList<>();
                for(ChessPiece piece : testboard.blackPieces){
                    if(!piece.getRemoved())
                        blackMoves.addAll(piece.getMoves());
                    //Removing enPassant from any pawn who already has it. enPassant only lasts one turn
                    if(piece.getClass() == Pawn.class){
                        Pawn pawn = (Pawn)piece;
                        pawn.enPassant = false;
                    }
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
                //WIN CONDITIONS
                whiteMoves = new ArrayList<>();
                for(ChessPiece piece : testboard.whitePieces){
                    if(!piece.getRemoved())
                        whiteMoves.addAll(piece.getMoves());
                    //Removing enPassant from any pawn who already has it. enPassant only lasts one turn
                    if(piece.getClass() == Pawn.class){
                        Pawn pawn = (Pawn)piece;
                        pawn.enPassant = false;
                    }
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
            }
            System.out.println((testboard.turn%2 == 0 ? "Black " : "White ") + "Turn: " + testboard.turn);
            System.out.println(testboard);

            /*
            ADD IN PLAYER.GET MOVE
             */
            //White's turn
            if(testboard.turn%2 == 1){
                Move whiteMove = white.getMove(testboard);
                if(whiteMove == null){
                    answer = "stop";
                }
                //Checking if the move is a castle
                else if(whiteMove.piece.getClass() == King.class && testboard.board[whiteMove.pos.x][whiteMove.pos.y].pieceHold != null
                        && testboard.board[whiteMove.pos.x][whiteMove.pos.y].pieceHold.getClass() == Rook.class){
                    castle(testboard, whiteMove.piece.getLocation(), whiteMove.pos);
                }
                else{
                    movePiece(testboard, whiteMove);
                }
            }
            //Black's turn
            else{
                Move blackMove = white.getMove(testboard);
                if(blackMove == null){
                    answer = "stop";
                }
                //Checking if the move is a castle
                else if(blackMove.piece.getClass() == King.class && testboard.board[blackMove.pos.x][blackMove.pos.y].pieceHold != null
                        && testboard.board[blackMove.pos.x][blackMove.pos.y].pieceHold.getClass() == Rook.class){
                    castle(testboard, blackMove.piece.getLocation(), blackMove.pos);
                }
                else{
                    movePiece(testboard, blackMove);
                }
            }


            //If it is black's turn
            if(testboard.turn%2==0){
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
    Castle will castle two pieces if it doesn't have any pieces in between and returns true if it does
     */
    private static boolean castle(ChessBoard curr, Location kingPos, Location rookPos){
        //Which direction to check spaces for
        int direction = 1;
        if(kingPos.x > rookPos.x)
            direction = -1;

        //Creating the new locations
        Location kingNew = new Location(kingPos.x+(2*direction), kingPos.y);
        Location rookNew = new Location(kingPos.x+direction, kingPos.y);

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
    private static void movePiece(ChessBoard curr, Move currMove){
        Location pos = currMove.pos;

        curr.board[currMove.piece.getLocation().x][currMove.piece.getLocation().y].pieceHold = null;
        if(curr.board[pos.x][pos.y].pieceHold != null)
            removePiece(curr.board[pos.x][pos.y].pieceHold, curr);
        curr.board[pos.x][pos.y].pieceHold = currMove.piece;
        currMove.piece.setLocation(pos.x, pos.y);
    }

    /*
    Remove piece from board
     */
    private static void removePiece(ChessPiece piece, ChessBoard curr){
        curr.board[piece.getLocation().x][piece.getLocation().y].pieceHold = null;
        piece.setRemoved(true);
    }

    /*
    A function that checks for enPassant on a chess board after a move is done.
    It looks for any pawns that have enPassant as true and checks if there is an enemy pawn behind it.
    If it has an enemy pawn behind it, the pawn with enPassant is removed.
    This situation can only occur as an end result of an enPassant
     */
    private static void enPassant(ChessBoard curr){
        for(ChessPiece piece : curr.whitePieces){
            if(piece.getClass() == Pawn.class){
                Pawn pawn = (Pawn)piece;
                ChessPiece behind = curr.board[pawn.getLocation().x][pawn.getLocation().y-1].pieceHold;
                if(pawn.enPassant && behind!=null && behind.getClass() == Pawn.class && behind.getColor() != pawn.getColor()){
                    removePiece(pawn, curr);
                }
            }
        }
        for(ChessPiece piece : curr.blackPieces){
            if(piece.getClass() == Pawn.class){
                Pawn pawn = (Pawn)piece;
                ChessPiece behind = curr.board[pawn.getLocation().x][pawn.getLocation().y+1].pieceHold;
                if(pawn.enPassant && behind!= null && behind.getClass() == Pawn.class && behind.getColor() != pawn.getColor()){
                    removePiece(pawn, curr);
                }
            }
        }
    }
}
