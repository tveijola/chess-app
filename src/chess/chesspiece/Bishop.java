package chess.chesspiece;

import java.util.ArrayList;
import java.util.List;

import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.coordinates.GridCoord;
import javafx.scene.image.Image;

//CLASS
//Bishop class that extends the Piece class
public class Bishop extends Piece {
	
	
	// CONSTRUCTORS
	public Bishop(Image im, GridCoord coord, String col) {
		super(im, "bishop", col, 3, coord);
	}
	public Bishop(GridCoord coord, String col, int moves) {
		super("bishop", col, 3, coord, moves);
	}
	
	
	//METHODS

	public Piece copyPiece() {
		GridCoord tmpCoord = new GridCoord(this.getCoord());
		return new Bishop(tmpCoord, this.getColor(), this.getMoveCount());
	}

	public ChessMove[] computeAvailableMoves(Chessboard board) {
		
		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int[] checkSquare = new int[2];
		
		GridCoord src = this.getCoord();
		
		int verticalDirection;
		int horizontalDirection;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 8; j++) {
				
				if (i == 0) {verticalDirection = -1; horizontalDirection = -1;}
				else if (i == 1) {verticalDirection = -1; horizontalDirection = 1;}
				else if (i == 2) {verticalDirection = 1; horizontalDirection = -1;}
				else {verticalDirection = 1; horizontalDirection = 1;}
				
				checkSquare[0] = src.getGridCoord()[0] + j * horizontalDirection;
				checkSquare[1] = src.getGridCoord()[1] + j * verticalDirection;
				
				if (isBoardSquare(checkSquare)) {
					GridCoord dest = new GridCoord(checkSquare);
					if (!board.getSquare(dest).getIsOccupied()) {
						listOfAvailableMoves.add(new ChessMove(src, dest, this, false));
					} else {
						Piece targetPiece = board.getSquare(dest).getOccupyingPiece();
						targetPieceColor  = targetPiece.getColor();
						if (!pieceColor.equals(targetPieceColor)) {
							listOfAvailableMoves.add(new ChessMove(src, dest, this, targetPiece, true));
						}
						break;
					}
				} else {
					break;
				}
			}
		}

		ChessMove[] availableMoves = new ChessMove[listOfAvailableMoves.size()];
		availableMoves = listOfAvailableMoves.toArray(availableMoves);
		return availableMoves;
	}
}