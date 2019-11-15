package chess.player;

import java.util.Random;

import chess.ChessApplication;
import chess.chessmove.ChessMove;

public class PlayerAI extends Player {
	
	public PlayerAI (String col) {
		super(col, "Computer");
	}
	
	public ChessMove getNextMove (ChessApplication app) {
		this.evaluatePosition(app);
		return this.selectMove(app);
	}
	
	private double evaluatePosition (ChessApplication app) {
		return 0;
	}
	
	private ChessMove selectMove (ChessApplication app) {
		
		Random r = new Random();
		int numPieces = app.getChessgame().getPieceListMap().size();
		int selectPieceIndex = r.nextInt(numPieces);
		
		int numMoves = app.getChessgame().getMoveListMap().get(this.getPlayerColor()).get(selectPieceIndex).length;
		int selectMoveIndex = r.nextInt(numMoves);
		ChessMove selectedMove = app.getChessgame().getMoveListMap().get(this.getPlayerColor()).get(selectPieceIndex)[selectMoveIndex];
		
		return selectedMove;
	}
	
}
