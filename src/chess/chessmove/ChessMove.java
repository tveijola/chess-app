package chess.chessmove;

import java.util.List;

import chess.ChessApplication;
import chess.chessboard.BoardSquare;
import chess.chesspiece.Piece;
import chess.coordinates.GridCoord;

//CLASS

public class ChessMove {
	
	
	// INSTANCE VARIABLES
	private GridCoord moveSrc;
	private GridCoord moveDest;
	private Piece     movingPiece;
	private Piece     capturedPiece;
	private boolean   isCapture;
	private boolean   isLegal = true;
	private boolean   isEnPassantCapture = false;
	private boolean   isCastling = false;
	
	
	// CONSTRUCTORS
	public ChessMove(GridCoord src, GridCoord dest, Piece p, boolean isCap) {
		this.moveSrc       = new GridCoord(src);
		this.moveDest      = new GridCoord(dest);
		this.movingPiece   = p;
		this.capturedPiece = null;
		this.isCapture     = isCap;
	}
	public ChessMove(GridCoord src, GridCoord dest, Piece p, Piece capturedP, boolean isCap) {
		this.moveSrc       = new GridCoord(src);
		this.moveDest      = new GridCoord(dest);
		this.movingPiece   = p;
		this.capturedPiece = capturedP;
		this.isCapture     = isCap;
	}
	
	
	// GETTERS AND SETTERS
	public GridCoord getMoveSrc() {
		return this.moveSrc;
	}
	public void setMoveSrc(GridCoord src) {
		this.moveSrc = src;
	}

	public GridCoord getMoveDest() {
		return this.moveDest;
	}
	public void setMoveDest(GridCoord dest) {
		this.moveDest = dest;
	}

	public Piece getMovingPiece () {
		return this.movingPiece;
	}
	public void setMovingPiece (Piece p) {
		this.movingPiece = p;
	}
	
	public Piece getCapturedPiece () {
		return this.capturedPiece;
	}
	public void setCapturedPiece (Piece p) {
		this.capturedPiece = p;
	}

	public boolean isCapture() {
		return this.isCapture;
	}
	public void setIsCapture(boolean isCap) {
		this.isCapture = isCap;
	}

	public boolean isLegal() {
		return this.isLegal;
	}
	public void setIsLegal(boolean isLeg) {
		this.isLegal = isLeg;
	}
	
	public boolean isEnPassantCapture() {
		return this.isEnPassantCapture;
	}
	public void setIsEnPassantCapture(boolean isEnPass) {
		this.isEnPassantCapture = isEnPass;
	}
	
	public boolean isCastling() {
		return this.isCastling;
	}
	public void setIsCastling(boolean isCastle) {
		this.isCastling = isCastle;
	}
	
	public static ChessMove parseMove (Piece movingPiece, BoardSquare destSq, ChessApplication app) {
		String color = movingPiece.getColor();

		GridCoord moveSrc  = movingPiece.getCoord();
		GridCoord moveDest = destSq.getCoord();
		ChessMove tmpMove;

		List<ChessMove[]> moveList = app.getChessgame().getMoveListMap().get(color);
		for (int i = 0; i < moveList.size(); i++) {
			for (int j = 0; j < moveList.get(i).length; j++) {
				tmpMove = moveList.get(i)[j];
				if (tmpMove.getMoveDest().equals(moveDest) && tmpMove.getMoveSrc().equals(moveSrc)) {
					return tmpMove;
				}
			}
		}
		// The following is for the case that an illegal move was made.
		boolean isCapture = (app.getChessgame().getGameBoard().getSquare(moveDest).getIsOccupied()) ? true : false;
		return new ChessMove(moveSrc, moveDest, movingPiece, isCapture);
	}
	
	public static String determineMoveNotation (ChessMove move, ChessApplication app) {

		String pieceNotation;
		String moveNotation;
		Piece movingPiece = move.getMovingPiece();
		switch(movingPiece.getName()) {
		case "pawn":
			pieceNotation = ""; break;
		case "knight":
			pieceNotation = "N"; break;
		case "bishop":
			pieceNotation = "B"; break;
		case "rook":
			pieceNotation = "R"; break;
		case "queen":
			pieceNotation = "Q"; break;
		case "king":
			pieceNotation = "K"; break;
		default:
			pieceNotation = ""; break;
		}
		String prefix = "";
		String capture = "";

		int destX = move.getMoveDest().getGridCoord()[1];

		if (movingPiece.getName().equals("king") && move.isCastling()) {
			if (destX == 6) {
				if(app.getGameState().getTurnNumber() % 2 == 0) {
					return "O-O ... ";
				} else {
					return "O-O\n";
				}


			} else if (destX == 2) {
				if(app.getGameState().getTurnNumber() % 2 == 0) {
					return "O-O-O ... ";
				} else {
					return "O-O-O\n";
				}
			}
		}

		int numPiecesWithMove = countPiecesWithMove(move, app);
		if (numPiecesWithMove > 1) {
			prefix = move.getMoveSrc().getAlgNotation();
		}

		if(move.isCapture()) {
			if (move.getMovingPiece().getName().equals("pawn")) {
				String src = move.getMoveSrc().getAlgNotation();
				capture = src.substring(0, 1) + "x";
			} else {
				capture = "x";
			}
		}

		if (app.getGameState().getTurnNumber() % 2 == 0) {
			moveNotation =  pieceNotation + prefix + capture + move.getMoveDest().getAlgNotation() + " ... ";
		} else {
			moveNotation =  pieceNotation + prefix + capture + move.getMoveDest().getAlgNotation() + "\n";
		}
		return moveNotation;
	}
	
	public static int countPiecesWithMove(ChessMove move, ChessApplication app) {

		int result = 0;
		String movingPieceName = move.getMovingPiece().getName();
		String col = app.getGameState().getColorToMove();

		List<ChessMove[]>  moveList  = app.getChessgame().getMoveListMap().get(col);
		List<Piece> pieceList = app.getChessgame().getPieceListMap().get(col);

		for (int i = 0; i < moveList.size(); i++) {
			for (int j = 0; j < moveList.get(i).length; j++) {
				if (moveList.get(i)[j].getMoveDest().equals(move.getMoveDest()) && pieceList.get(i).getName().equals(movingPieceName)) {
					result += 1;
				}
			}
		}

		return result;
	}
}