package chess.chesspiece;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import chess.coordinates.GridCoord;
import chess.ChessApplication;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import javafx.scene.image.Image;

//CLASS
//Pawn class that extends the Piece class
public class Pawn extends Piece {
	
	
	// INSTANCE VARIABLES
	private boolean isCapturableEnPassant;
	
	
	
	// CONSTRUCTORS
	public Pawn(Image im, GridCoord coord, String col) {
		super(im, "pawn", col, 1, coord);
		this.isCapturableEnPassant = false;
	}
	public Pawn(GridCoord coord, String col, int moves) {
		super("pawn", col, 1, coord, moves);
		this.isCapturableEnPassant = false;
	}
	
	
	// GETTERS AND SETTERS
	public void setIsCapturableEnPassant(boolean exp) {
		this.isCapturableEnPassant = exp;
	}
	public boolean getIsCapturableEnPassant() {
		return this.isCapturableEnPassant;
	}
	
	
	// METHODS
	public Piece copyPiece() {
		GridCoord tmpCoord = this.getCoord();
		return new Pawn(tmpCoord, this.getColor(), this.getMoveCount());
	}
	
	
	// Method to compute available moves and captures for a pawn
	public ChessMove[] computeAvailableMoves(Chessboard board) {
		
		List<ChessMove> listOfAvailableMoves = new ArrayList<ChessMove>();
		String pieceColor = this.getColor();
		String targetPieceColor;
		int forwardOne;
		int enPassantRow;
		int[] checkSquare = new int[2];
		int[] horizontalMove = {0,-1,1};
		
		GridCoord src = this.getCoord();
		
		if (pieceColor.equals("white")){
			forwardOne = -1;
			enPassantRow = 3;
		} else if (pieceColor.equals("black")) {
			forwardOne = 1;
			enPassantRow = 4;
		} else {
			forwardOne = 0;
			enPassantRow = -1;
		}
		
		
		for(int i = 0; i < horizontalMove.length; i++) {
			checkSquare[0] = src.getGridCoord()[0] + horizontalMove[i];
			checkSquare[1] = src.getGridCoord()[1] + forwardOne;
			if (isBoardSquare(checkSquare)) {
				GridCoord dest = new GridCoord(checkSquare);
				// Normal move
				if (i == 0 && !board.getSquare(dest).getIsOccupied()) {
					listOfAvailableMoves.add(new ChessMove(src, dest, this, false));
					checkSquare[1] += forwardOne;
					dest = new GridCoord(checkSquare);
					if (this.getMoveCount() == 0 && !board.getSquare(dest).getIsOccupied()) {
						listOfAvailableMoves.add(new ChessMove(src, dest, this, false));
						this.setIsCapturableEnPassant(true);
					}
				// Normal capture
				} else if (i > 0 && board.getSquare(dest).getIsOccupied()) {
					Piece targetPiece = board.getSquare(dest).getOccupyingPiece();
					targetPieceColor = targetPiece.getColor();
					if (!pieceColor.equals(targetPieceColor)){
						listOfAvailableMoves.add(new ChessMove(src, dest, this, targetPiece, true));
					}
				}
				// Capture En passant
				if (i > 0 && this.getCoord().getGridCoord()[1] == enPassantRow){
					checkSquare[1] -= forwardOne;
					dest = new GridCoord(checkSquare);
					if (board.getSquare(dest).getIsOccupied()) {
						Piece targetPiece = board.getSquare(dest).getOccupyingPiece();
						targetPieceColor = targetPiece.getColor();
						if (!pieceColor.equals(targetPieceColor)){
							if (board.getSquare(dest).getOccupyingPiece().getIsCapturableEnPassant()){
								checkSquare[1] += forwardOne;
								dest = new GridCoord(checkSquare);
								ChessMove enPassantCap = new ChessMove(src, dest, this, targetPiece, true);
								enPassantCap.setIsEnPassantCapture(true);
								listOfAvailableMoves.add(enPassantCap);
							}
						}
					}
				}
			}
		}
		
		ChessMove[] availableMoves = new ChessMove[listOfAvailableMoves.size()];
		availableMoves = listOfAvailableMoves.toArray(availableMoves);
		return availableMoves;
	}
	
	
	public static void promote (ChessApplication app, Pawn p, String promotion) throws FileNotFoundException {
		
		Chessboard board = app.getChessgame().getGameBoard();
		GridCoord pawnCoord = p.getCoord();
		String pawnColor = p.getColor();
		
		Piece promotedPiece = null;
		Image piece_im = new Image(new FileInputStream(app.getImageDirectory() + pawnColor + "_" + promotion + ".png"));
		
		switch (promotion) {
		case "queen":
			promotedPiece = new Queen(piece_im, p.getCoord(), p.getColor());
			break;
		case "rook":
			promotedPiece = new Rook(piece_im, p.getCoord(), p.getColor());
			break;
		case "bishop":
			promotedPiece = new Bishop(piece_im, p.getCoord(), p.getColor());
			break;
		case "knight":
			promotedPiece = new Knight(piece_im, p.getCoord(), p.getColor());
			break;
		default:
			break;
		}
		
		// Remove pawn from piece list, node parent (Chessboard), clear chessboard square
		board.getSquare(pawnCoord).clearSquare();
		board.getChildren().remove(p);
		app.getChessgame().getPieceListMap().get(pawnColor).remove(p);
		
		// Place promoted piece to piece list, node parent, chessboard square
		app.getChessgame().getPieceListMap().get(p.getColor()).add(promotedPiece);
		board.addPieceToChessboard(promotedPiece, app);
		
	}
	
	
}