package chess.graphics;

import chess.chessboard.Chessboard;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoardScreenManager {

	private StackPane  boardScreen;
	private GridPane   background;
	private Chessboard chessboard;

	public BoardScreenManager (double BOARDSCREEN_WIDTH, double BOARDSCREEN_HEIGHT, double CHESSBOARD_WIDTH, double CHESSBOARD_HEIGHT, double PIECE_SCALE) {

		double squareWidth  = CHESSBOARD_WIDTH  / 8;
		double squareHeight = CHESSBOARD_HEIGHT / 8;

		this.boardScreen = new StackPane();
		this.boardScreen.setMaxHeight(BOARDSCREEN_WIDTH);
		this.boardScreen.setMaxWidth(BOARDSCREEN_HEIGHT);

		this.background = new GridPane();
		this.addSquaresToBackground(squareWidth, squareHeight);

		this.chessboard = new Chessboard(CHESSBOARD_WIDTH, CHESSBOARD_HEIGHT, PIECE_SCALE);

		this.addBackgroundToTable();
		this.addChessBoardToTable();
	}

	public StackPane getBoardScreen() {
		return this.boardScreen;
	}

	public GridPane getBackground() {
		return this.background;
	}

	public Chessboard getChessboard() {
		return this.chessboard;
	}

	// ADD BORDER SQUARES TO BACKGROUND
	private void addSquaresToBackground (double squareWidth, double squareHeight) {

		int[] squaresToFill;
		int[] rownumbers  = {8,7,6,5,4,3,2,1};
		String[] colnames = {"a", "b", "c", "d", "e", "f", "g", "h"};

		// Create border squares
		for (int i = 0; i < 10; i++) {

			ColumnConstraints column = new ColumnConstraints(squareWidth);
			RowConstraints row = new RowConstraints(squareHeight);
			column.setHalignment(HPos.CENTER);
			row.setValignment(VPos.CENTER);
			this.background.getColumnConstraints().add(column);
			this.background.getRowConstraints().add(row);

			squaresToFill = (i == 0 || i == 9) ? new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}: new int[] {0, 9};

			for (int j : squaresToFill) {
				Rectangle rect = new Rectangle(squareWidth, squareHeight);
				rect.setFill(Color.BURLYWOOD);
				this.background.add(rect, j, i);
			}
		}

		// Add row and column identifiers to the border squares
		for (int i = 0; i < 8; i++) {

			Text txt_col_w = new Text(colnames[i]);
			Text txt_col_b = new Text(colnames[i]);
			Text txt_row_w = new Text(Integer.toString(rownumbers[i]));
			Text txt_row_b = new Text(Integer.toString(rownumbers[i]));
			txt_col_w.setFont(Font.font(20));
			txt_col_b.setFont(Font.font(20)); txt_col_b.setVisible(false);
			txt_row_w.setFont(Font.font(20));
			txt_row_b.setFont(Font.font(20)); txt_row_b.setVisible(false);

			this.background.add(txt_row_w, 0, i + 1);
			this.background.add(txt_row_b, 9, i + 1);
			this.background.add(txt_col_w, i + 1, 9);
			this.background.add(txt_col_b, i + 1, 0);	
		}
	}
	
	private void addBackgroundToTable () {
		this.boardScreen.getChildren().add(this.background);
		StackPane.setAlignment(this.background, Pos.CENTER);
	}

	private void addChessBoardToTable () {
		this.boardScreen.getChildren().add(this.chessboard);
		StackPane.setAlignment(this.chessboard, Pos.CENTER);
	}
}
