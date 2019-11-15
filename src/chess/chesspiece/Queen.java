package chess.chesspiece;

import java.util.ArrayList;
import java.util.List;

import chess.coordinates.GridCoord;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import javafx.scene.image.Image;

//CLASS
//Queen class that extends the Piece class
public class Queen extends Piece {
	
	
	// CONSTRUCTORS ----------------------
	public Queen(Image im, GridCoord coord, String col) {
		super(im, "queen", col, 9, coord);
	}
	public Queen(GridCoord coord, String col, int moves) {
		super("queen", col, 9, coord, moves);
	}

	
	// METHODS --------------------------
	// Returns a deep copy of the piece
	public Piece copyPiece() {
		GridCoord tmpCoord = new GridCoord(this.getCoord());
		return new Queen(tmpCoord, this.getColor(), this.getMoveCount());
	}

	// Returns available moves for the piece as an array of AvailableMove objects
	public ChessMove[] computeAvailableMoves(Chessboard board) {
		
		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int[] checkSquare = new int[2];
		int verticalDirection;
		int horizontalDirection;
		
		GridCoord src = this.getCoord();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 1; j < 8; j++) {
				
				switch(i){
					case 0: verticalDirection = -1; horizontalDirection = -1; break;
					case 1: verticalDirection = -1; horizontalDirection = 0;  break;
					case 2: verticalDirection = -1; horizontalDirection = 1;  break;
					case 3: verticalDirection = 1;  horizontalDirection = -1; break;
					case 4: verticalDirection = 1;  horizontalDirection = 0;  break;
					case 5: verticalDirection = 1;  horizontalDirection = 1;  break;
					case 6: verticalDirection = 0;  horizontalDirection = 1;  break;
					case 7: verticalDirection = 0;  horizontalDirection = -1; break;
					default: 
						System.out.println("Unexpected.");
						verticalDirection = -1; horizontalDirection = -1; break;
				}
				
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