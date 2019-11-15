package chess.chessboard;

import java.util.List;
import java.util.Map;

import chess.ChessApplication;
import chess.GameCoordinator;
import chess.chesspiece.Piece;
import chess.coordinates.GridCoord;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

//CLASS
//Chessboard class that consits of Squares
public class Chessboard extends GridPane {

	double width;
	double height;
	double squareWidth;
	double squareHeight;
	double pieceScale;

	// CONSTRUCTORS
	public Chessboard () {

	}
	public Chessboard(double w, double h, double s) {

		this.width  = w;
		this.height = h;

		this.setMaxHeight(h);
		this.setMaxWidth(w);

		this.squareWidth  = w / 8;
		this.squareHeight = h / 8;

		this.pieceScale = s;
		this.createBoardSquares();
	}


	// GETTERS AND SETTERS
	public BoardSquare getSquare (GridCoord coord) {
		int col = coord.getGridCoord()[0];
		int row = coord.getGridCoord()[1];

		Node result = null;
		ObservableList<Node> children = this.getChildren();
		for (Node node : children) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col && node instanceof BoardSquare) {
				return (BoardSquare) node;
			}
		}
		return (BoardSquare) result;
	}
	public Piece getPiece (GridCoord coord) {
		int col = coord.getGridCoord()[0];
		int row = coord.getGridCoord()[1];

		Node result = null;
		ObservableList<Node> children = this.getChildren();
		for (Node node : children) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col && node instanceof Piece) {
				return (Piece) node;
			}
		}
		return (Piece) result;
	}

	// METHODS

	// CREATE BOARD SQUARES
	private void createBoardSquares () {
		
		int colorTracker  = 0;
		
		for (int i = 0; i < 8; i++) {

			ColumnConstraints column = new ColumnConstraints(this.squareWidth);
			column.setHalignment(HPos.CENTER);
			this.getColumnConstraints().add(column);
			RowConstraints row = new RowConstraints(this.squareHeight);
			row.setValignment(VPos.CENTER);
			this.getRowConstraints().add(row);

			for (int j = 0; j < 8; j++) {
				
				Paint col = ((colorTracker + i) % 2 == 0) ? Color.LIGHTGRAY : Color.DARKGRAY;
				BoardSquare sq = new BoardSquare(squareWidth, squareHeight, new GridCoord(new int[] {j, i}), col);
				sq.setFill(col);
				
				colorTracker += 1;
				this.assignActionToSquare(sq);
				this.add(sq, j, i);
			}
		}
	}

	// ASSIGN MOUSE EVENT TO A BOARD SQUARES
	private void assignActionToSquare (BoardSquare sq) {

		final Paint initColor = sq.getFill();
		sq.setOnMousePressed(pressed -> {
			if (pressed.getButton() == MouseButton.SECONDARY && !pressed.isPrimaryButtonDown()) {
				Paint newFill = (sq.getFill() == Color.RED) ? initColor : Color.RED;
				sq.setFill(newFill);
			}
		});
	}
	
	public void fillChessboard (Map<String, List<Piece>> pieceListMap, ChessApplication app) {

		for (String s : new String[] {"white", "black"}) {
			List<Piece> pieceList = pieceListMap.get(s);
			for (int i = 0; i < pieceList.size(); i++) {

				Piece p = pieceList.get(i);

				double rescale = (p.getName().equals("pawn")) ? 0.8 : 1;
				p.setFitHeight(this.squareHeight * this.pieceScale * rescale);
				p.setFitWidth (this.squareWidth * this.pieceScale * rescale);

				GameCoordinator.assignActionToPieces(p, app);
				int y = p.getCoord().getGridCoord()[0];
				int x = p.getCoord().getGridCoord()[1];
				this.getSquare(p.getCoord()).setOccupyingPiece(p);
				this.add(p, y, x);

			}
		}
	}
	
	public void addPieceToChessboard (Piece p, ChessApplication app) {
		double rescale = (p.getName().equals("pawn")) ? 0.8 : 1;
		p.setFitHeight(this.squareHeight * this.pieceScale * rescale);
		p.setFitWidth (this.squareWidth * this.pieceScale * rescale);

		GameCoordinator.assignActionToPieces(p, app);
		int y = p.getCoord().getGridCoord()[0];
		int x = p.getCoord().getGridCoord()[1];
		this.getSquare(p.getCoord()).setOccupyingPiece(p);
		this.add(p, y, x);
	}

	public Chessboard copyChessboard(ChessApplication app) {
		Chessboard copyBoard = new Chessboard(this.width, this.height, this.pieceScale);
		return copyBoard;
	}

}