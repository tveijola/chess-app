package chess.graphics;

import java.util.List;

import chess.ChessApplication;
import chess.chessboard.BoardSquare;
import chess.chessmove.ChessMove;
import chess.chesspiece.Piece;
import chess.coordinates.GridCoord;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class NodeManipulator {
	
	// Get piece node by coordinates
	public static Node getPieceAtIndex (int column, int row, GridPane gp) {
	   
		Node result = null;
		ObservableList<Node> children = gp.getChildren();
		for (Node node : children) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof Piece) {
				result = node;
				break;
			}
		}
		return result;
	}
	
	// Set pieces as visible in coordinates
	public static void showPieceAtIndex (int column, int row, GridPane gp) {
		ObservableList<Node> children = gp.getChildren();
		for (Node node : children) {
			if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column && node instanceof Piece) {
				node.setVisible(true);
			}
		}
	}
	
	// Update piece coordinates
	public static void updatePieceCoordinates(Piece p, Node dest, GridPane gp) {
		int colIndex = GridPane.getColumnIndex(dest);
		int rowIndex = GridPane.getRowIndex(dest);
		GridPane.setColumnIndex(p, colIndex);
		GridPane.setRowIndex(p, rowIndex);
	}
	
	// Set PieceImage coordinates
	public static void setPieceNodeCoordinates (Piece p, GridCoord coord) {
		GridPane.setColumnIndex(p, coord.getGridCoord()[0]);
		GridPane.setRowIndex(p, coord.getGridCoord()[1]);
	}
	
	// Flip the board (visual only, no effect on coordinates)
	public static void flipBoard (GridPane gp) {
		ObservableList<Node> nodes = gp.getChildren();
		
		for (Node n : nodes) {
			if (n instanceof Text) {
				boolean exp = (n.isVisible()) ? false : true;
				n.setVisible(exp);
			}
		}
		
		double angle = (gp.getRotate() == 0) ? 180 : 0;
		gp.setRotate(angle);
		setNodeRotation(gp, angle);
	}
	
	// Set rotation angle for nodes
	public static void setNodeRotation (GridPane gp, double angle) {
		ObservableList<Node> nodes = gp.getChildren();
		for (Node n : nodes) {
			n.setRotate(angle);
		}
	}
	
	// Add text to a text area
	public static void addTxtToTextArea (TextArea txtArea, String txt) {
		txtArea.appendText(txt);
	}
	
	
	// Highlight available moves for dragged piece
	public static void highlightMoves (ChessApplication app, Piece p) {
		
		List<Piece> pieceList = app.getChessgame().getPieceListMap().get(p.getColor());
		int index = -1;
		
		for (int i = 0; i < pieceList.size(); i++) {
			if (p.equals(pieceList.get(i))) {
				index = i;
				break;
			}
		}
		
		ChessMove [] moves = app.getChessgame().getMoveListMap().get(p.getColor()).get(index);
		
		for (ChessMove m : moves) {
			if (m.isLegal()) {
				app.getChessboard().getSquare(m.getMoveDest()).setFill(Color.ORANGERED);
			}
		}
	}
	
	// Reset square colors (return highlighted squares back to normal)
	public static void resetSquareFills (ChessApplication app) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BoardSquare sq = app.getChessboard().getSquare(new GridCoord(new int[] {i,j}));
				sq.setFill(sq.getInitialFillColor());
			}
		}
	}
}
