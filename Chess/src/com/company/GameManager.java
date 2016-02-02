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
                white = new Human(true);
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
                black = new Human(false);
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
            generateAllMoves(testboard);
            checkForCheck(testboard);
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
                else if(whiteMove.piece.getClass() == Pawn.class && whiteMove.pos.y == 7){
                    movePiece(testboard, whiteMove);
                    white.upgradePawn(testboard, whiteMove.piece);
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
            curr.removePiece(curr.board[pos.x][pos.y].pieceHold);
        curr.board[pos.x][pos.y].pieceHold = currMove.piece;
        currMove.piece.setLocation(pos.x, pos.y);
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
                    curr.removePiece(pawn);
                }
            }
        }
        for(ChessPiece piece : curr.blackPieces){
            if(piece.getClass() == Pawn.class){
                Pawn pawn = (Pawn)piece;
                ChessPiece behind = curr.board[pawn.getLocation().x][pawn.getLocation().y+1].pieceHold;
                if(pawn.enPassant && behind!= null && behind.getClass() == Pawn.class && behind.getColor() != pawn.getColor()){
                    curr.removePiece(pawn);
                }
            }
        }
    }

    /*
    Generates moves for all pieces on a board
     */
    private static void generateAllMoves(ChessBoard curr){
        curr.checkPiece = null;
        for(ChessPiece piece : curr.whitePieces){
            if(!piece.getRemoved())
                piece.generateMoves(curr);
        }
        for(ChessPiece piece : curr.blackPieces){
            if(!piece.getRemoved())
                piece.generateMoves(curr);
        }
    }

    /*
    Checks for if there is a king in check and removes moves from pieces that does not uncheck the king.
     */
    private static void checkForCheck(ChessBoard curr){
        if(curr.checkPiece!=null){
            System.out.println((curr.checkPiece.getColor() ? "White's " : "Black's ") + "King is in check!");
            //If its king piece is in check, then remove moves that don't uncheck it
            if(curr.checkPiece.getColor()){
                for(ChessPiece piece : curr.whitePieces){
                    if(!piece.getRemoved()) {
                        for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext();) {
                            Location move = iterator.next();
                            if (!curr.removesCheck(piece, move)) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
            else{
                for(ChessPiece piece : curr.blackPieces){
                    if(!piece.getRemoved()) {
                        for (Iterator<Location> iterator = piece.getMoves().iterator(); iterator.hasNext();) {
                            Location move = iterator.next();
                            if (!curr.removesCheck(piece, move)) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
    }
}
