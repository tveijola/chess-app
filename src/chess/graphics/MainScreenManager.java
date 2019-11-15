package chess.graphics;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MainScreenManager {

	private BorderPane mainScreen;
	private InfoScreenManager infoScreen;
	private BoardScreenManager boardScreen;

	public MainScreenManager (double INFOSCREEN_WIDTH, double INFOSCREEN_HEIGHT, 
							  double BOARDSCREEN_WIDTH, double BOARDSCREEN_HEIGHT, 
							  double CHESSBOARD_WIDTH, double CHESSBOARD_HEIGHT, double PIECE_SCALE) {

		this.mainScreen  = new BorderPane();
		this.boardScreen = new BoardScreenManager(BOARDSCREEN_WIDTH, BOARDSCREEN_HEIGHT,CHESSBOARD_WIDTH, CHESSBOARD_HEIGHT, PIECE_SCALE);
		this.infoScreen  = new InfoScreenManager(INFOSCREEN_WIDTH, INFOSCREEN_HEIGHT);
		this.infoScreen.setButtonActions(this.boardScreen.getChessboard(), this.boardScreen.getBackground());
		
		this.assembleMainWindow();
	}

	public BorderPane getMainScreen() {
		return this.mainScreen;
	}
	public InfoScreenManager getInfoScreen() {
		return this.infoScreen;
	}
	public BoardScreenManager getBoardScreen() {
		return this.boardScreen;
	}
	

	// CREATE THE MAIN WINDOW
	public void assembleMainWindow () {

		// Set the background fill of the main window
		this.mainScreen.setBackground(new Background(new BackgroundFill(Color.PERU, null, null)));

		// Add the BorderPane and Vertical Box to the main window
		this.mainScreen.setCenter(this.boardScreen.getBoardScreen());
		this.mainScreen.setRight(this.infoScreen.getInfoScreen());
	}

}
