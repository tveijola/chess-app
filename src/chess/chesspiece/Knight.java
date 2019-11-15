package chess.chesspiece;

import java.util.ArrayList;
import java.util.List;

import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.coordinates.GridCoord;
import javafx.scene.image.Image;

//CLASS
//Knight class that extends the Piece class
public class Knight extends Piece {
	
	
	// CONSTRUCTORS
	public Knight(Image im, GridCoord coord, String col) {
		super(im, "knight", col, 2, coord);
	}
	public Knight(GridCoord coord, String col, int moves) {
		super("knight", col, 2, coord, moves);
	}
	
	
	// METHODS
	public Piece copyPiece() {
		GridCoord tmpCoord = new GridCoord(this.getCoord());
		return new Knight(tmpCoord, this.getColor(), this.getMoveCount());
	}
	
	public ChessMove[] computeAvailableMoves(Chessboard board) {
		
		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int[] checkSquare = new int[2];
		
		GridCoord src = this.getCoord();

		int[] verticalMove   = {-2, -2, 2, 2, -1, -1, 1, 1};
		int[] horizontalMove = {1, -1, 1, -1, 2, -2, 2, -2};
		
		for (int i = 0; i < verticalMove.length; i++) {
			checkSquare[0] = src.getGridCoord()[0] + horizontalMove[i];
			checkSquare[1] = src.getGridCoord()[1] + verticalMove[i];
			
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

		ChessMove[] availableMoves = new ChessMove[listOfAvailableMoves.size()];
		availableMoves = listOfAvailableMoves.toArray(availableMoves);
		return availableMoves;
	}
}