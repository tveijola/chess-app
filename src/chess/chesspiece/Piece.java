package chess.chesspiece;

import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import chess.ChessApplication;
import chess.chessboard.BoardSquare;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.coordinates.GridCoord;
import chess.graphics.PromoteScreenManager;

public abstract class Piece extends ImageView {
	
	private String name;
	private String color;
	private GridCoord coord;
	private final int value;
	private int moveCount = 0;
	
	public Piece (Image im, String n, String c, int val, GridCoord gCoord) {
		super(im);
		this.name = n;
		this.color = c;
		this.value = val;
		this.coord = gCoord;
	}
	public Piece (String n, String c, int val, GridCoord gCoord, int moves) {
		super();
		this.name = n;
		this.color = c;
		this.value = val;
		this.coord = gCoord;
		this.moveCount = moves;
	}
	
	
	public String getName () {
		return this.name;
	}
	public void setName (String n) {
		this.name = n;
	}
	
	public String getColor () {
		return this.color;
	}
	public void setColor (String c) {
		this.color = c;
	}
	
	public GridCoord getCoord () {
		return this.coord;
	}
	public void setCoord (GridCoord coord) {
		this.coord = new GridCoord(coord);
	}
	
	public int getValue () {
		return this.value;
	}
	
	public int getMoveCount () {
		return this.moveCount;
	}
	public void setMoveCount (int n) {
		this.moveCount = n;
	}
	
	
	
	
	
	// SHARED METHODS
	public void increaseMoveCount () {
		this.moveCount += 1;
	}
	public void decreaseMoveCount () {
		this.moveCount -= 1;
	}
	
	// Workaround to enable Pawn method to be called from Piece object
	// Pawn class overrides this, should never be called for any other piece.
	public void setIsCapturableEnPassant(boolean exp){
		
	}
	public boolean getIsCapturableEnPassant () {
		return false;
	}
	
	public boolean equals (Piece p) {
		
		if (!this.getName().equals(p.getName()))      {return false;}
		if (!this.getColor().equals(p.getColor()))    {return false;}
		if (!this.getCoord().equals(p.getCoord()))    {return false;}
		if ( this.getValue() != p.getValue())         {return false;}
		if ( this.getMoveCount() != p.getMoveCount()) {return false;}
		
		return true;
	}
	
	// Check if coordinates are on the chessboard
	public static boolean isBoardSquare (int[] coord) {
		int col = coord[0];
		int row = coord[1];
		if (row < 0 || row > 7 || col < 0 || col > 7) {
			return false;
		} 
		return true;
	}
	
	public static void makeMove (ChessMove move, ChessApplication app) {
		
		Chessboard board = app.getChessgame().getGameBoard();
		
		Piece movingPiece = move.getMovingPiece();
		
		GridCoord src  = move.getMoveSrc();
		GridCoord dest = move.getMoveDest();
		
		BoardSquare srcSq  = board.getSquare(src);
		BoardSquare destSq = board.getSquare(dest);
		
		/*
		 * 1. Clear destination square
		 * 	- BoardSquare method clearSquare();
		 *  - GridPane method remove(Piece);
		 *  - Remove piece from pieceList
		 *  - Add captured piece to list of captured pieces
		 *  
		 * 2. Set new coordinates for moving piece
		 *  - Clear source square
		 *  - BoardSquare setOccupyingPiece(Piece);
		 *  - Update Piece coordinates setCoord(dest);
		 *  - Update piece move count
		 * */
		
		if (move.isCapture()) {
			Piece capturedPiece = move.getCapturedPiece();
			board.getSquare(capturedPiece.getCoord()).clearSquare();
			board.getChildren().remove(capturedPiece);
			app.getChessgame().getPieceListMap().get(capturedPiece.getColor()).remove(capturedPiece);
			app.getChessgame().getCapturedPieceListMap().get(capturedPiece.getColor()).add(capturedPiece);
		}
		
		srcSq.clearSquare();
		int col = dest.getGridCoord()[0];
		int row = dest.getGridCoord()[1];
		GridPane.setColumnIndex(movingPiece, col);
		GridPane.setRowIndex(movingPiece, row);
		
		destSq.setOccupyingPiece(movingPiece);
		movingPiece.setCoord(dest);
		movingPiece.increaseMoveCount();
		
		if (movingPiece instanceof Pawn && ( row == 0 || row == 7)) {
			PromoteScreenManager.createPromoteScreen(app, (Pawn) movingPiece);
		}
		
	}

	
	
	// ABSTRACT METHODS (SUBCLASSES MUST IMPLEMENT)
	public abstract Piece copyPiece();
	public abstract ChessMove[] computeAvailableMoves(Chessboard board);
}
