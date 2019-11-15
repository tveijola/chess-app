package chess;

import java.io.FileNotFoundException;
import java.util.List;

import chess.ChessGame;
import chess.chessboard.BoardSquare;
import chess.chessboard.Chessboard;
import chess.chessmove.ChessMove;
import chess.chesspiece.Piece;
import chess.graphics.MainScreenManager;
import chess.graphics.NodeManipulator;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ChessApplication extends Application {
	
	/* ----------------------------------------------------------------------------------------
	 * INSTANCE VARIABLES
	 * ---------------------------------------------------------------------------------------- */
	
	
	private ChessGameState gamestate;
	private MainScreenManager mainScreen;
	private ChessGame chessgame;
	private Chessboard chessboard;
	private int turnNumToPrint = 1;
	private final String programDirectory;
	private final String imageDirectory;
	private final int WINDOW_WIDTH = 1400;
	private final int WINDOW_HEIGHT = 1000;
	private final int BOARDSCREEN_WIDTH = 1000;
	private final int BOARDSCREEN_HEIGHT = 1000;
	private final int CHESSBOARD_WIDTH = 800;
	private final int CHESSBOARD_HEIGHT = 800;
	private final int INFOSCREEN_WIDTH = 400;
	private final int INFOSCREEN_HEIGHT = 1000;
	private final double pieceScale;
	private final double SQUARE_WIDTH;
	private final double SQUARE_HEIGHT;
	
	
	
	/* ----------------------------------------------------------------------------------------
	 * CONSTRUCTOR
	 * ---------------------------------------------------------------------------------------- */
	
	public ChessApplication () throws FileNotFoundException {
		
		// Set piece scaling factor and square width and height
		this.pieceScale = 0.8;
		this.SQUARE_WIDTH  = CHESSBOARD_WIDTH  / 8;
		this.SQUARE_HEIGHT = CHESSBOARD_HEIGHT / 8;
		
		// Directory variables
		this.programDirectory = System.getProperty("user.dir");
		this.imageDirectory   = programDirectory + "\\resources\\png\\";
		
		this.mainScreen = new MainScreenManager(this.INFOSCREEN_WIDTH, this.INFOSCREEN_HEIGHT, 
												this.BOARDSCREEN_WIDTH, this.BOARDSCREEN_HEIGHT, 
												this.CHESSBOARD_WIDTH, this.CHESSBOARD_HEIGHT, this.pieceScale);
		
		// Save a reference to the chessboard
		this.chessboard = this.mainScreen.getBoardScreen().getChessboard();
		
		// Instantiate Chessgame object that contains the game rules and mechanics
		this.chessgame = new ChessGame(this.chessboard, this.imageDirectory);
		
		// Instantiate GameState object that contains the current game state.
		this.gamestate = new ChessGameState();
	}
	
	
	
	/* ----------------------------------------------------------------------------------------
	 * GETTERS
	 * ---------------------------------------------------------------------------------------- */
	
	public MainScreenManager getMainScreenManager () {
		return this.mainScreen;
	}
	public ChessGameState getGameState() {
		return this.gamestate;
	}
	public ChessGame getChessgame() {
		return this.chessgame;
	}
	public Chessboard getChessboard () {
		return this.chessboard;
	}
	public String getImageDirectory() {
		return this.imageDirectory;
	}
	public double getPieceScale() {
		return this.pieceScale;
	}
	public double getSquareWidth() {
		return this.SQUARE_WIDTH;
	}
	public double getSquareHeight() {
		return this.SQUARE_HEIGHT;
	}
	public void setTurnNumToPrint (int n) {
		this.turnNumToPrint = n;
	}
	public int getTurnNumToPrint () {
		return this.turnNumToPrint;
	}
	public boolean getOptions (String s) {
		return this.getMainScreenManager().getInfoScreen().getOptCheckBox(s).isSelected();
	}
	
	/* ----------------------------------------------------------------------------------------
	 * MAIN METHOD
	 * ---------------------------------------------------------------------------------------- */
	
	public static void main(String args[]){
		launch(args);
	}
	
	
	
	/* ----------------------------------------------------------------------------------------
	 * OVERRIDE START METHOD OF EXTENDED APPLICATION CLASS
	 * ---------------------------------------------------------------------------------------- */
	
	public void start(Stage primaryStage) throws FileNotFoundException {
		
		// Instantiate a ChessApplication object that contains everything that is needed
		ChessApplication chessApp = new ChessApplication();
		
		chessApp.getChessgame().getGameBoard().fillChessboard(chessApp.getChessgame().getPieceListMap(), chessApp);
		ObservableList<Node> children = chessApp.getChessgame().getGameBoard().getChildren();
		for (Node node : children) {
			if(node instanceof BoardSquare) {
				GameCoordinator.assignActionToSquare((BoardSquare) node, chessApp);
			}
		}
		chessApp.getChessgame().setMoveListMap(chessApp.getChessgame().createInitialMoveListMap());
		
		// Set the scene
		MainScreenManager msm = chessApp.getMainScreenManager();
		Scene scene = new Scene(msm.getMainScreen(), chessApp.WINDOW_WIDTH, chessApp.WINDOW_HEIGHT);
		
		
		TextArea moveLogger = msm.getInfoScreen().getMoveLogger();
		TextArea textLogger = msm.getInfoScreen().getTextLogger();
		
		List<Piece> whitePieces = chessApp.chessgame.getPieceListMap().get("white");
		List<ChessMove[]> whiteMoves = chessApp.chessgame.getMoveListMap().get("white");
		
		
		// Prepare the movelogger.
		NodeManipulator.addTxtToTextArea(moveLogger, "> Game start.\n> " + Integer.toString(this.turnNumToPrint) + ". ");
		
		
		// Print out available moves for white
		NodeManipulator.addTxtToTextArea(textLogger, "> WHITE TO MOVE\n");
		for (int i = 0; i < whitePieces.size(); i++) {
			Piece p = whitePieces.get(i);
			NodeManipulator.addTxtToTextArea(textLogger, "> " + p.getName() + " at " + p.getCoord().getAlgNotation() + " moves: ");
			for (int j = 0; j < whiteMoves.get(i).length; j++) {
				String algNot = whiteMoves.get(i)[j].getMoveDest().getAlgNotation();
				NodeManipulator.addTxtToTextArea(textLogger, algNot + ", ");
			}
			NodeManipulator.addTxtToTextArea(textLogger, "\n");
		}
		
		// Setting stage
		primaryStage.setTitle("Chessgame dev");
		primaryStage.setScene(scene);
		primaryStage.setWidth(chessApp.WINDOW_WIDTH);
		primaryStage.setHeight(chessApp.WINDOW_HEIGHT + 28);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}

}
