package chess.chesspiece;

import java.util.ArrayList;
import java.util.List;

import chess.ChessApplication;
import chess.ChessGame;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.coordinates.GridCoord;
import javafx.scene.image.Image;

//CLASS
//King class that extends the Piece class
public class King extends Piece {


	// INSTANCE VARIABLES ----------------
	private boolean isInCheck;


	// CONSTRUCTORS ----------------------
	public King(Image im, GridCoord coord, String col) {
		super(im, "king", col, 9999, coord);
		this.isInCheck = false;
	}
	public King(GridCoord coord, String col, int moves) {
		super("king", col, 9999, coord, moves);
		this.isInCheck = false;
	}

	//METHODS ---------------------------
	public void setIsInCheck(boolean exp) {
		this.isInCheck = exp;
	}
	public boolean getIsInCheck() {
		return this.isInCheck;
	}

	// Returns a deep copy of the piece
	public Piece copyPiece() {
		GridCoord tmpCoord = new GridCoord(this.getCoord());
		return new King(tmpCoord, this.getColor(), this.getMoveCount());
	}

	// Returns availableMoves as an array of AvailableMove objects
	public ChessMove[] computeAvailableMoves(Chessboard board) {

		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int[] checkSquare = new int[2];

		int[] vert = {-1, 0, 1};
		int[] horz = {-1, 0, 1};

		GridCoord src = this.getCoord();

		for (int i = 0; i < vert.length; i++) {
			for (int j = 0; j < horz.length; j++) {

				if (vert[i] == 0 && horz[j] == 0) {
					continue;
				}

				checkSquare[0] = src.getGridCoord()[0] + horz[j];
				checkSquare[1] = src.getGridCoord()[1] + vert[i];
				
				if (isBoardSquare(checkSquare)) {
					GridCoord dest = new GridCoord(checkSquare);
					if (!board.getSquare(dest).getIsOccupied()) {
						listOfAvailableMoves.add(new ChessMove(src, dest, this, false));
					} else {
						Piece targetPiece = board.getSquare(dest).getOccupyingPiece();
						targetPieceColor = targetPiece.getColor();
						if (!pieceColor.equals(targetPieceColor)) {
							listOfAvailableMoves.add(new ChessMove(src, dest, this, targetPiece, true));
						}
					}
				}
			}
		}

		ChessMove[] availableMoves = new ChessMove[listOfAvailableMoves.size()];
		availableMoves = listOfAvailableMoves.toArray(availableMoves);
		return availableMoves;
	}



	public void checkCastling(ChessGame chessgame, ChessApplication app) {

		Chessboard board = app.getChessgame().getGameBoard();

		if(this.getMoveCount() == 0) {

			List<ChessMove> castelingMoves = new ArrayList<ChessMove>();
			int[] kingSquare  = this.getCoord().getGridCoord();
			int[] checkSquare = new int[2];

			// SHORT CASTLE
			boolean shortCastle = true;
			checkSquare[0] = kingSquare[0];
			checkSquare[1] = kingSquare[1];

			GridCoord dest;

			for (int i = 1; i < 3; i++) {
				checkSquare[0] += 1;
				dest = new GridCoord(checkSquare);
				if (board.getSquare(dest).getIsOccupied() || squareIsAttacked(dest, this.getColor(), chessgame)) {
					shortCastle = false; break;
				}
			}
			if (shortCastle) {
				checkSquare[0] = this.getCoord().getGridCoord()[0] + 3;
				dest = new GridCoord(checkSquare);
				if (board.getSquare(dest).getOccupyingPiece() instanceof Rook) {
					Rook kingRook = (Rook) board.getSquare(dest).getOccupyingPiece();
					if (kingRook.getColor().equals(this.getColor()) && kingRook.getMoveCount() == 0) {
						int[] castleSquare = {kingSquare[0] + 2, kingSquare[1]};
						ChessMove castleMove = new ChessMove(new GridCoord(kingSquare), new GridCoord(castleSquare), this, false);
						castleMove.setIsCastling(true);
						castelingMoves.add(castleMove);
					}
				}
			}

			// LONG CASTLE
			boolean longCastle = true;
			checkSquare[0] = kingSquare[0];
			checkSquare[1] = kingSquare[1];

			for (int i = 1; i < 4; i++) {
				checkSquare[0] -= 1;
				dest = new GridCoord(checkSquare);
				if (board.getSquare(dest).getIsOccupied()) {
					longCastle = false; break;
				}
				if (i < 3) {
					if (squareIsAttacked(dest, this.getColor(), chessgame)) {
						longCastle = false; break;
					}
				}
			}
			if (longCastle) {
				checkSquare[0] = this.getCoord().getGridCoord()[0] - 4;
				dest = new GridCoord(checkSquare);
				if (board.getSquare(dest).getOccupyingPiece() instanceof Rook) {
					Rook queenRook = (Rook) board.getSquare(dest).getOccupyingPiece();
					if (queenRook.getColor().equals(this.getColor()) && queenRook.getMoveCount() == 0) {
						int[] castleSquare = {kingSquare[0] - 2, kingSquare[1]};
						ChessMove castleMove = new ChessMove(new GridCoord(kingSquare), new GridCoord(castleSquare), this, false);
						castleMove.setIsCastling(true);
						castelingMoves.add(castleMove);
					}
				}
			}

			// ADD CASTELING MOVES TO MOVELIST

			int indexOfKing = chessgame.findKingIndex(this.getColor());
			List<ChessMove[]> movelist = chessgame.getMoveListMap().get(this.getColor());
			ChessMove[] oldKingMoves = movelist.get(indexOfKing);

			List<ChessMove> newKingMoves = new ArrayList<ChessMove>();
			for (int i = 0; i < oldKingMoves.length; i++) {
				newKingMoves.add(oldKingMoves[i]);
			}
			newKingMoves.addAll(castelingMoves);

			ChessMove[] kingMoves = new ChessMove[newKingMoves.size()];
			kingMoves = newKingMoves.toArray(kingMoves);

			movelist.remove(indexOfKing);
			movelist.add(indexOfKing, kingMoves);

		}
	}


	private boolean squareIsAttacked(GridCoord sq, String kingColor, ChessGame chessgame) {

		String oppColor = (kingColor.equals("white")) ? "black" : "white";

		List<ChessMove[]>  oppMoves = chessgame.getMoveListMap().get(oppColor);
		List<Piece>       oppPieces = chessgame.getPieceListMap().get(oppColor);

		for (int i = 0; i < oppPieces.size(); i++) {
			for (int j = 0; j < oppMoves.get(i).length; j++) {
				if(oppMoves.get(i)[j].getMoveDest().equals(sq)) {
					return true;
				}
			}
		}
		return false;
	}


}