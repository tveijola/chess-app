package chess.chesspiece;

import java.util.ArrayList;
import java.util.List;

import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.coordinates.GridCoord;
import javafx.scene.image.Image;

//CLASS
//Rook class that extends the Piece class
public class Rook extends Piece {
	
	// CONSTRUCTORS
	public Rook(Image im, GridCoord coord, String col) {
		super(im, "rook", col, 5, coord);
	}
	public Rook(GridCoord coord, String col, int moves) {
		super("rook", col, 5, coord, moves);
	}
	
	// METHODS
	public Piece copyPiece() {
		GridCoord tmpCoord = new GridCoord(this.getCoord());
		return new Rook(tmpCoord, this.getColor(), this.getMoveCount());
	}
	
	public ChessMove[] computeAvailableMoves(Chessboard board) {
		
		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int[] checkSquare = new int[2];
		int verticalDirection;
		int horizontalDirection;
		
		GridCoord src = this.getCoord();
		
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j < 8; j++) {
				
				if (i == 0) {verticalDirection = -1; horizontalDirection = 0;}
				else if (i == 1) {verticalDirection = 1; horizontalDirection = 0;}
				else if (i == 2) {verticalDirection = 0; horizontalDirection = -1;}
				else {verticalDirection = 0; horizontalDirection = 1;}
				
				checkSquare[0] = src.getGridCoord()[0] + j * horizontalDirection;
				checkSquare[1] = src.getGridCoord()[1] + j * verticalDirection;
				
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