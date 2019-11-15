package chess;

import chess.chessmove.ChessMove;

public class ChessGameState {
	
	
	// INSTANCE VARIABLES
	private boolean gameContinues;
	private ChessMove previousMove;
	private ChessMove currentMove;
	private boolean whiteKingInCheck;
	private boolean blackKingInCheck;
	private int turnNumber;
	private final String[] turnTracker = {"white", "black"};
	private String colorToMove;
	
	
	
	// CONSTRUCTORS
	public ChessGameState () {
		this.gameContinues = true;
		this.previousMove = null;
		this.currentMove = null;
		this.whiteKingInCheck = false;
		this.blackKingInCheck = false;
		this.turnNumber = 0;
		this.colorToMove = this.turnTracker[turnNumber % 2];
	}
	
	
	// GETTERS AND SETTERS
	public void setGameContinues (boolean exp) {
		this.gameContinues = exp;
	}
	public boolean doesGameContinue () {
		return this.gameContinues;
	}
	
	public void setPreviousMove (ChessMove move) {
		this.previousMove = move;
	}
	public ChessMove getPreviousMove () {
		return this.previousMove;
	}
	
	public void setCurrentMove (ChessMove move) {
		this.currentMove = move;
	}
	public ChessMove getCurrentMove () {
		return this.currentMove;
	}
	
	public void setWhiteKingInCheck (boolean exp) {
		this.whiteKingInCheck = exp;
	}
	public boolean isWhiteKingInCheck () {
		return this.whiteKingInCheck;
	}
	
	public void setBlackKingInCheck (boolean exp) {
		this.blackKingInCheck = exp;
	}
	public boolean isBlackKingInCheck () {
		return this.blackKingInCheck;
	}
	
	public void setTurnNumber (int n) {
		this.turnNumber = n;
	}
	public int getTurnNumber () {
		return this.turnNumber;
	}
	
	public void setColorToMove (String c) {
		this.colorToMove = c;
	}
	public String getColorToMove () {
		return this.colorToMove;
	}
	
	public String[] getTurnTracker () {
		return this.turnTracker;
	}
}
