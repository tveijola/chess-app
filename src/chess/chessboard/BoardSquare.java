package chess.chessboard;

import chess.chesspiece.Piece;
import chess.coordinates.GridCoord;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

//CLASS
//Square class that describes a specific square on the chessboard
public class BoardSquare extends Rectangle {
	
	
	// INSTANCE VARIABLES
	private final GridCoord coord;
	private Piece occupyingPiece;
	private boolean isOccupied;
	private final Paint initialFillColor;
	
	
	// CONSTRUCTORS
	public BoardSquare (double w, double h, GridCoord gCoord, Paint fillColor) {
		super(w, h);
		this.coord          = new GridCoord(gCoord);
		this.occupyingPiece = null;
		this.isOccupied     = false;
		initialFillColor    = fillColor;
	}
	
	
	// GETTERS AND SETTERS
	public GridCoord getCoord() {
		return this.coord;
	}
	
	public void setOccupyingPiece(Piece p) {
		if (this.isOccupied) {
			System.out.println("The square is already occupied!");
		} else {
			this.occupyingPiece = p;
			this.isOccupied = true;
		}
	}
	public Piece getOccupyingPiece() {
		if (this.isOccupied){
			return this.occupyingPiece;
		} else {
			return null;
		}
	}
	
	public boolean getIsOccupied() {
		return this.isOccupied;
	}
	
	public Paint getInitialFillColor () {
		return this.initialFillColor;
	}
	
	
	
	// METHODS
	public void clearSquare () {
		this.occupyingPiece = null;
		this.isOccupied = false;
	}
	
	/*
	public BoardSquare copySquare() {
		GridCoord tmpCoord = new GridCoord(this.coord);
		BoardSquare copySquare = new BoardSquare(this.getWidth(), this.getHeight(), tmpCoord, this.getInitialFillColor());
		if(this.isOccupied){
			copySquare.setOccupyingPiece(this.getOccupyingPiece().copyPiece());
		}
		return copySquare;
	}*/
}